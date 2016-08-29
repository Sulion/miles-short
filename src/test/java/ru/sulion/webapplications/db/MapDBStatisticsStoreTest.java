package ru.sulion.webapplications.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.core.KeyComposer;

import javax.ws.rs.core.Response;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sulion on 27.08.16.
 */
public class MapDBStatisticsStoreTest {

    private static final String TEST_DB = "stats_test.db";
    private MapDBStatisticsStore statisticsStore;
    private DB db;
    public static final String TEST_SHORT_URL = "xYswIE";
    public static final String TEST_URL = "https://github.com/Sulion/miles-short";



    @Before
    public void setUp() throws Exception {
        db = DBMaker.fileDB(TEST_DB).closeOnJvmShutdown().fileMmapEnable().cleanerHackEnable()
                .transactionEnable().make();
        statisticsStore = new MapDBStatisticsStore(db, new KeyComposer(db));
    }

    @After
    public void tearDown() throws Exception {
        db.close();
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    public void registerRequest() throws Exception {
        Redirect redirect = new Redirect(Response.Status.FOUND, TEST_URL, TEST_SHORT_URL);
        statisticsStore.registerRequest(redirect);
        List<Redirect> request = Collections.singletonList(redirect);
        assertEquals(Long.valueOf(1), statisticsStore.requestStatistics(request)
                .get(redirect.getLocation().toString()));
    }

    @Test
    public void retrieveStats_NoStatsCase_EmptyList() throws Exception {
        Map<String, Long> map = statisticsStore.requestStatistics(new ArrayList<>());
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void retrieveStats_NoStatsCase_NoValuesInMap() throws Exception {
        Map<String, Long> map = statisticsStore.requestStatistics(new ArrayList<Redirect>(){{
            add(new Redirect(Response.Status.FOUND, "https://yandex.ru", "gXGL02"));
            add(new Redirect(Response.Status.FOUND, "https://google.com", "gXGL03"));
        }});
        assertNotNull(map);
        assertEquals(2, map.size());
        map.values().forEach(k -> assertEquals(0L, k.longValue()));
    }

}