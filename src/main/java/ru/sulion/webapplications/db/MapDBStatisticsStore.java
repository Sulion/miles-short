package ru.sulion.webapplications.db;

import org.mapdb.DB;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.core.KeyComposer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;


public class MapDBStatisticsStore implements StatisticsStore {
    private final DB db;
    private final KeyComposer keyComposer;
    /**
     * Heavy traffic (for example, 5k rps over a year) will overwhelm Integer,
     * so we need a long to store the value.
     */

    public MapDBStatisticsStore(DB db, KeyComposer keyComposer) {
        this.db = db;
        this.keyComposer = keyComposer;
    }
    //DBMap rots after each commit
    private Map<String, LongAdder> openMap(){
        return (Map<String, LongAdder>) db.treeMap(STATISTICS_DICT)
                .keySerializer(Serializer.STRING).createOrOpen();
    }

    @Override
    public boolean registerRequest(Redirect redirect) {
        try {
            String key = keyComposer.toStatsKey(redirect);
            final Map<String, LongAdder> statistics = openMap();
            statistics.computeIfAbsent(key, k -> new LongAdder()).increment();
            return true;
        } finally {
            db.commit();
        }
    }

    @Override
    public Map<String, Long> requestStatistics(List<Redirect> request) {
        final Map<String, LongAdder> statistics = openMap();
        return request.stream().map(keyComposer::toStatsKey)
                .collect(Collectors.toMap(keyComposer::removePrefix, k -> statistics.get(k).longValue()));
    }
}
