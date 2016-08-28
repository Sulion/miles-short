package ru.sulion.webapplications.db;

import com.google.inject.Inject;
import org.mapdb.DB;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.core.KeyComposer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MapDBStatisticsStore implements StatisticsStore {
    /**
     * Heuristic constant making StatisticsStore to dump current counters into a permanent storage.
     * Might be parametrized in future versions
     */
    public static final long COMMIT_INTERVAL = 60000L;
    private final DB db;
    private final KeyComposer keyComposer;
    private final Map<String, LongAdder> workCache;
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private volatile long lastCommit = System.currentTimeMillis();

    /**
     * Heavy traffic (for example, 5k rps over a year) will overwhelm Integer,
     * so we need a long to store the value.
     */

    @Inject
    public MapDBStatisticsStore(DB db, KeyComposer keyComposer) {
        this.db = db;
        this.keyComposer = keyComposer;
        workCache = new ConcurrentHashMap<>();
    }

    //DBMap Map rots after each commit
    private Map<String, Long> openMap() {
        return db.treeMap(STATISTICS_DICT)
                .keySerializer(Serializer.STRING).valueSerializer(Serializer.LONG).createOrOpen();
    }

    @Override
    public boolean registerRequest(Redirect redirect) {
        String key = keyComposer.toStatsKey(redirect);
        underLock(() -> workCache.computeIfAbsent(key, v -> new LongAdder()).increment(), cacheLock.readLock());
        //We could've written only on reads, but in case we crash, something should be left
        if(System.currentTimeMillis() - lastCommit > COMMIT_INTERVAL)
            underLock(this::dumpStatisticCache, cacheLock.writeLock());
        return true;
    }

    private void dumpStatisticCache() {
        final Map<String, Long> statistics = openMap();
        workCache.entrySet().forEach(e -> statistics.put(e.getKey(),
                statistics.getOrDefault(e.getKey(), 0L) + e.getValue().longValue()));
        workCache.clear();
        lastCommit = System.currentTimeMillis();
        db.commit();
    }

    private void underLock(Runnable function, Lock lock) {
        try {
            lock.lock();
            function.run();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<String, Long> requestStatistics(List<Redirect> request) {
        underLock(this::dumpStatisticCache, cacheLock.writeLock());
        final Map<String, Long> statistics = openMap();
        return request.stream().map(keyComposer::toStatsKey).collect(Collectors.toMap(keyComposer::removePrefix, statistics::get));
    }
}
