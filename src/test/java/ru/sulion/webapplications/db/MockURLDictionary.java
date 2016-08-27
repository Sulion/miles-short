package ru.sulion.webapplications.db;

import ru.sulion.webapplications.api.Redirect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sulion on 27.08.16.
 */
public class MockURLDictionary implements RedirectDictionary {

    private final Map<String, Redirect> map = new HashMap<>();

    public MockURLDictionary(Map<String, Redirect> initMap) {
        map.putAll(initMap);
    }

    @Override
    public Redirect find(String shortUrl) {
        return map.get(shortUrl);
    }
}
