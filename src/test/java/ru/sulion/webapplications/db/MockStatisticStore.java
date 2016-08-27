package ru.sulion.webapplications.db;

import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.db.StatisticsStore;

import java.util.HashMap;
import java.util.List;
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
    public boolean registerRequest(Redirect redirect) {
        longMap.put(redirect.getShortUrl() + redirect.getLocation().toString(),
                longMap.getOrDefault(redirect, new AtomicLong(1)));
        return true;
    }

    @Override
    public Map<String, Long> requestStatistics(List<String> request) {
        return null;
    }
}
