package ru.sulion.webapplications.core;

import org.junit.After;
import org.junit.Test;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.db.MockStatisticStore;
import ru.sulion.webapplications.db.MockURLDictionary;
import ru.sulion.webapplications.db.RedirectDictionary;
import ru.sulion.webapplications.db.StatisticsStore;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by sulion on 27.08.16.
 */
public class TracedRedirectingServiceTest {

    public static final String SHORT_URL = "xYswIE";
    private TracedRedirectingService service;

    @org.junit.Before
    public void setUp() throws Exception {
        RedirectDictionary dictionary = new MockURLDictionary(new HashMap<String, Redirect>() {{
            put(SHORT_URL, new Redirect(Response.Status.FOUND,
                    "https://github.com/Sulion/miles-short", SHORT_URL));
        }});
        StatisticsStore statisticsStore = new MockStatisticStore();
        OverheadTaskManager taskManager = new MockTaskManager();
        service = new TracedRedirectingService(dictionary, statisticsStore, taskManager);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void resolveURI() throws Exception {
        Redirect redirect = service.resolveURI("xYswIE");
        assertEquals(URI.create("https://github.com/Sulion/miles-short"), redirect.getLocation());
        assertEquals(Response.Status.FOUND, redirect.getStatus());
    }

    @Test
    public void resolveAbsentURI() {
        Redirect redirect = service.resolveURI("NoSuchURL");
        assertEquals(Response.Status.NOT_FOUND, redirect.getStatus());
    }



}