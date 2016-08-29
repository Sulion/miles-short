package ru.sulion.webapplications.core;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by sulion on 28.08.16.
 */
public class KeyComposerTest {
    public static final String GOOGLE_URL = "https://google.com";
    private static final String TEST_DB = "KeyComposerTest.db";
    private KeyComposer keyComposer;
    @Before
    public void setUp() throws Exception {
        DB db = DBMaker.fileDB(TEST_DB).closeOnJvmShutdown().fileMmapEnable().cleanerHackEnable().make();
        keyComposer = new KeyComposer(db);
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(TEST_DB));
    }

    @Test
    public void toShortURL() throws Exception {
        String s = keyComposer.toShortURL(GOOGLE_URL);
        assertEquals(6, s.length());
        String s2 = keyComposer.toShortURL(GOOGLE_URL);
        assertNotSame(s, s2);
        assertTrue(s2.compareTo(s) > 0);
        assertTrue(StringUtils.isAlphanumeric(s));
        System.out.println(s + ":" + s2);
        System.out.println(keyComposer.fromBase62(s));
        System.out.println(keyComposer.fromBase62(s2));
    }

    @Test
    public void testConvertToBase62_PositiveCases() {
        String key = keyComposer.toBase62(System.currentTimeMillis()%1000000000L/100L*62*62);
        System.out.println(key);
        System.out.println(keyComposer.fromBase62(key));
    }

    @Test
    public void toStatsKey() throws Exception {

    }

    @Test
    public void removePrefix() throws Exception {

    }

}