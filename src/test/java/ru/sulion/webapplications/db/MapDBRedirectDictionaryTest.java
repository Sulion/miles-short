package ru.sulion.webapplications.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by sulion on 27.08.16.
 */
public class MapDBRedirectDictionaryTest {

    public static final String TEST_DB = "test.db";
    public static final String TEST_URL = "https://github.com/Sulion/miles-short";
    public static final String TEST_SHORT_URL = "xYswIE";
    private DB db;

    @Before
    public void setUp() throws Exception {
        db = DBMaker.fileDB(TEST_DB).closeOnJvmShutdown().fileMmapEnable().make();
        Map<String, Redirect> map = (Map<String, Redirect>) db.treeMap(RedirectDictionary.DICT_NAME)
                .keySerializer(Serializer.STRING)
                .createOrOpen();
        map.put(TEST_SHORT_URL, new Redirect(Response.Status.FOUND, TEST_URL));
        db.close();
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(TEST_DB));
    }

    @Test
    public void find() throws Exception {
        RedirectDictionary dictionary = new MapDBRedirectDictionary(DBMaker.fileDB(TEST_DB)
                .closeOnJvmShutdown().readOnly().make());
        assertNotNull( dictionary.find(TEST_SHORT_URL));
        assertEquals(URI.create(TEST_URL),
                dictionary.find(TEST_SHORT_URL).getLocation());
    }

}