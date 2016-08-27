package ru.sulion.webapplications.db;

import org.mapdb.DB;
import org.mapdb.Serializer;
import org.omg.CORBA.Object;
import ru.sulion.webapplications.api.Redirect;

import java.util.Map;


public class MapDBStatisticsStore implements StatisticsStore {
    private final DB db;

    public MapDBStatisticsStore(DB db) {
        this.db = db;
    }

    @Override
    public boolean registerRequest(Redirect redirect) {
        try {
            Map<String, ? extends Object> statistics = (Map<String, ? extends Object>) db.treeMap(STATISTICS_DICT)
                    .keySerializer(Serializer.STRING).createOrOpen();
            return false;
        } finally {
            db.commit();
        }
    }
}
