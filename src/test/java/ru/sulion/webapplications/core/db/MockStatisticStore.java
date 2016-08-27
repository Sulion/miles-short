package ru.sulion.webapplications.core.db;

import ru.sulion.webapplications.db.StatisticsStore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by sulion on 27.08.16.
 */
public class MockStatisticStore implements StatisticsStore{

    private final Map<String, AtomicLong> longMap;

    public MockStatisticStore() {
        this.longMap = new HashMap<>();
    }

    @Override
    public boolean registerRequest(String shortUrl) {
        longMap.put(shortUrl, longMap.getOrDefault(shortUrl, new AtomicLong(1)));
        return true;
    }
}
