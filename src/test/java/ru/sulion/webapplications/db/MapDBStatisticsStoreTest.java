package ru.sulion.webapplications.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by sulion on 27.08.16.
 */
public class MapDBStatisticsStoreTest {

    private static final String TEST_DB = "stats_test.db";
    private MapDBStatisticsStore statisticsStore;
    private DB db;
    public static final String TEST_SHORT_URL = "xYswIE";


    @Before
    public void setUp() throws Exception {
        db = DBMaker.fileDB(TEST_DB).closeOnJvmShutdown().fileMmapEnable().make();
        statisticsStore = new MapDBStatisticsStore(db);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    public void registerRequest() throws Exception {
//        statisticsStore.registerRequest()
    }

}