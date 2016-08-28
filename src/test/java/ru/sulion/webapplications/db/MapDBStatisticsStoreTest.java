package ru.sulion.webapplications.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.core.KeyComposer;

import javax.ws.rs.core.Response;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    public void registerRequest() throws Exception {
        Redirect redirect = new Redirect(Response.Status.FOUND, TEST_URL, TEST_SHORT_URL);
        statisticsStore.registerRequest(redirect);
        List<String> request = Collections.singletonList(redirect.getShortUrl() +
                redirect.getLocation().toString());
        assertEquals(Long.valueOf(1), statisticsStore.requestStatistics(request)
                .get(redirect.getLocation().toString()));
    }

}