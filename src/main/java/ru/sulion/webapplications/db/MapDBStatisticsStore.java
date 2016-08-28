package ru.sulion.webapplications.db;

import org.mapdb.DB;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.core.KeyComposer;

import java.util.List;
import java.util.Map;
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
    private Map<String, Long> openMap(){
        return db.treeMap(STATISTICS_DICT)
                .keySerializer(Serializer.STRING).valueSerializer(Serializer.LONG).createOrOpen();
    }

    @Override
    public boolean registerRequest(Redirect redirect) {
        try {
            String key = keyComposer.toStatsKey(redirect);
            final Map<String, Long> statistics = openMap();
            //DBMap protects each record with a reentrant RW-lock, so it's safe just to update the value
            statistics.put(key, statistics.getOrDefault(key, 0L)+1L);
            return true;
        } finally {
            db.commit();
        }
    }

    @Override
    public Map<String, Long> requestStatistics(List<Redirect> request) {
        final Map<String, Long> statistics = openMap();
        return request.stream().map(keyComposer::toStatsKey).collect(Collectors.toMap(keyComposer::removePrefix, statistics::get));
    }
}
