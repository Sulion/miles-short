package ru.sulion.webapplications.db;

import com.google.inject.Inject;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;
import org.mapdb.DB;

import java.util.Map;

/**
 * Created by sulion on 27.08.16.
 */
public class MapDBRedirectDictionary implements RedirectDictionary {
    private final DB db;
    Map<String, Redirect> storage;

    @Inject
    public MapDBRedirectDictionary(@ReadOnly DB db) {
        this.db = db;
        storage = (Map<String, Redirect>) db.treeMap(DICT_NAME).keySerializer(Serializer.STRING).open();
    }

    @Override
    public Redirect find(String shortUrl) {
        return storage.get(shortUrl);
    }
}
