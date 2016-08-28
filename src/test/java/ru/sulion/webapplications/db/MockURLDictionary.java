package ru.sulion.webapplications.db;

import ru.sulion.webapplications.api.Redirect;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sulion on 27.08.16.
 */
public class MockURLDictionary implements RedirectDictionary {

    public static final Redirect DEFAULT_VALUE = new Redirect(Response.Status.NOT_FOUND, null, null);
    private final Map<String, Redirect> map = new HashMap<>();

    public MockURLDictionary(Map<String, Redirect> initMap) {
        map.putAll(initMap);
    }

    @Override
    public Redirect find(String shortUrl) {
        return map.getOrDefault(shortUrl, DEFAULT_VALUE);
    }
}
