package ru.sulion.webapplications.db;

import com.google.inject.Inject;
import org.mapdb.DB;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;

import java.util.Map;

/**
 * Created by sulion on 27.08.16.
 */
public class MapDBRedirectDictionary implements RedirectDictionary {
    Map<String, Redirect> storage;

    @Inject
    public MapDBRedirectDictionary(@ReadOnly DB db) {
        storage = (Map<String, Redirect>) db.treeMap(DICT_NAME).keySerializer(Serializer.STRING).open();
    }

    @Override
    public Redirect find(String shortUrl) {
        return storage.get(shortUrl);
    }
}
