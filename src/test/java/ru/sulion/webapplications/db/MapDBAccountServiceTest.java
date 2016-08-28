package ru.sulion.webapplications.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.*;
import ru.sulion.webapplications.core.KeyComposer;

import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by sulion on 28.08.16.
 */
public class MapDBAccountServiceTest {
    private static final String TEST_DB = "account.db";
    public static final String ACCOUNT_NAME = "Seryy";
    public static final String SECOND_ACCOUNT_NAME = "Pakhan";
    public static final String YOUTUBE_COM = "https://youtube.com";
    private DB db;

    @Before
    public void setUp() throws Exception {
        db = DBMaker.fileDB(TEST_DB).closeOnJvmShutdown().fileMmapEnable().cleanerHackEnable()
                .transactionEnable().make();
        Map<String, Redirect> inMap = new HashMap<String, Redirect>(){{
            put("xYswIE", new Redirect(Response.Status.FOUND, "https://github.com/Sulion/miles-short", "xYswIE"));
            put("gXGL01", new Redirect(Response.Status.FOUND, "https://google.com", "gXGL01"));
            put("gXGL02", new Redirect(Response.Status.FOUND, "https://yandex.ru", "gXGL02"));
        }};
        Map<String, Redirect> seryyAccount = db.treeMap(MapDBAccountService.RECORDS_DB_PREFIX
                + ACCOUNT_NAME).keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA).createOrOpen();
        seryyAccount.putAll(inMap);
        db.commit();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
        Files.deleteIfExists(Paths.get(TEST_DB));
    }

    @Test
    public void getRecordsFor() throws Exception {
        MapDBAccountService accountService = new MapDBAccountService(db, null, null);
        List<Redirect> redirectList = accountService.getRecordsFor(ACCOUNT_NAME);
        assertNotNull(redirectList);
        assertEquals(3, redirectList.size());
        assertEquals(1, redirectList.stream().filter(k -> k.getShortUrl().equals("gXGL01")).count());
    }

    @Test
    public void registerRecordFor() throws Exception {
        MapDBAccountService accountService = new MapDBAccountService(db, new KeyComposer(db), "http://sho.rt");
        RegisterURLRequest registerURLRequest = new RegisterURLRequest(YOUTUBE_COM, Response.Status.FOUND);
        RegisteredURLResponse response = accountService.registerRecordFor(ACCOUNT_NAME, registerURLRequest);
        assertNotNull(response);
        assertNotNull(response.getShortUrl());
        assertEquals(20, response.getShortUrl().length());
        Map<String, Redirect> seryyAccount = db.treeMap(MapDBAccountService.RECORDS_DB_PREFIX
                +ACCOUNT_NAME).keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA).createOrOpen();
        Map<String, Redirect> globalMap = db.treeMap(RedirectDictionary.DICT_NAME).keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA).createOrOpen();
        assertEquals(4, seryyAccount.size());
        String key = response.getShortUrl();
        assertEquals(YOUTUBE_COM, globalMap.get(key.substring(key.length() - 6)).getLocation().toString());
    }

    @Test
    public void register_Success() throws Exception {
        MapDBAccountService accountService = new MapDBAccountService(db, new KeyComposer(db), "http://sho.rt");
        SignUpRequest request = new SignUpRequest(SECOND_ACCOUNT_NAME);
        SignUpResponse response = accountService.register(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getPassword());
    }

    @Test
    public void register_Failure() throws Exception {
        MapDBAccountService accountService = new MapDBAccountService(db, new KeyComposer(db), "http://sho.rt");
        SignUpRequest request = new SignUpRequest(SECOND_ACCOUNT_NAME);
        accountService.register(request);
        SignUpResponse response = accountService.register(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getPassword());
    }

    @Test
    public void authenticate_Success() throws MalformedURLException {
        MapDBAccountService accountService = new MapDBAccountService(db, new KeyComposer(db), "http://sho.rt");
        SignUpRequest request = new SignUpRequest(SECOND_ACCOUNT_NAME);
        SignUpResponse response = accountService.register(request);
        accountService.checkAccount(SECOND_ACCOUNT_NAME, response.getPassword());
    }

    @Test
    public void authenticate_Failure() throws MalformedURLException {
        MapDBAccountService accountService = new MapDBAccountService(db, new KeyComposer(db), "http://sho.rt");
        accountService.checkAccount(SECOND_ACCOUNT_NAME, "xxxxxxxx");
    }

}