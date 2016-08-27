package ru.sulion.webapplications.core;

import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.core.core.MockTaskManager;
import ru.sulion.webapplications.core.db.MockStatisticStore;
import ru.sulion.webapplications.core.db.MockURLDictionary;
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

    private TracedRedirectingService service;

    @org.junit.Before
    public void setUp() throws Exception {
        RedirectDictionary dictionary = new MockURLDictionary(new HashMap<String, Redirect>() {{
            put("xYswIE", new Redirect(Response.Status.FOUND, "https://github.com/Sulion/miles-short"));
        }});
        StatisticsStore statisticsStore = new MockStatisticStore();
        OverheadTaskManager taskManager = new MockTaskManager();
        service = new TracedRedirectingService(dictionary, statisticsStore, taskManager);
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void resolveURI() throws Exception {
        Redirect redirect = service.resolveURI("xYswIE");
        assertEquals(URI.create("https://github.com/Sulion/miles-short"), redirect.getLocation());
        assertEquals(Response.Status.FOUND, redirect.getStatus());
    }

}