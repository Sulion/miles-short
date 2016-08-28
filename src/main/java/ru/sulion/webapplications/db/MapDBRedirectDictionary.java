package ru.sulion.webapplications.db;

import com.google.inject.Inject;
import org.mapdb.DB;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by sulion on 27.08.16.
 */
public class MapDBRedirectDictionary implements RedirectDictionary {
    Map<String, Redirect> storage;
    public static final Redirect DEFAULT_VALUE = new Redirect(Response.Status.NOT_FOUND, null, null);


    @Inject
    public MapDBRedirectDictionary(@ReadOnly DB db) {
        storage = (Map<String, Redirect>) db.treeMap(DICT_NAME).keySerializer(Serializer.STRING).open();
    }

    @Override
    public Redirect find(String shortUrl) {
        return storage.getOrDefault(shortUrl, DEFAULT_VALUE);
    }
}
