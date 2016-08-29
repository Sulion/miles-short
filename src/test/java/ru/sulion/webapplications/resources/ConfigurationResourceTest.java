package ru.sulion.webapplications.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ru.sulion.webapplications.api.AccountService;
import ru.sulion.webapplications.api.RegisteredURLResponse;
import ru.sulion.webapplications.api.SignUpResponse;
import ru.sulion.webapplications.api.StatisticsStore;
import ru.sulion.webapplications.auth.MilesShortConfigAuthorizer;
import ru.sulion.webapplications.auth.MilesShortConfigurationAutheticator;
import ru.sulion.webapplications.core.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sulion on 29.08.16.
 */
public class ConfigurationResourceTest {

    private static final AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final StatisticsStore statisticsStore = mock(StatisticsStore.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(new AuthDynamicFeature(
                    new BasicCredentialAuthFilter.Builder<User>()
                            .setAuthenticator(new MilesShortConfigurationAutheticator(ACCOUNT_SERVICE))
                            .setAuthorizer(new MilesShortConfigAuthorizer())
                            .setRealm("URL MANAGEMENT")
                            .buildAuthFilter()))
            .addResource(new ConfigurationResource(statisticsStore, ACCOUNT_SERVICE))
            .build();
    String ACCOUNT_SETUP = "{ \"AccountId\": \"blah\" }";
    String INVALID_ACCOUNT_SETUP = "{ }";
    String URL_RECORD_SETUP_FULL = "{ \"url\": \"https://google.com\", \"redirectType\": 301 }";
    String URL_RECORD_SETUP_DFLT = "{ \"url\": \"https://google.com\" }";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void signUp_PositiveCase() throws Exception {

        when(ACCOUNT_SERVICE.register(anyObject()))
                .thenReturn(new SignUpResponse(true, "OK", "foobarre"));
        Response response = resources.client().target("/account").request()
                .accept(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.json(ACCOUNT_SETUP)).invoke();
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.hasEntity());
        SignUpResponse entity = response.readEntity(SignUpResponse.class);
        assertTrue(entity.isSuccess());
    }

    @Test
    public void signUp_NegativeCase() throws Exception {

        when(ACCOUNT_SERVICE.register(anyObject()))
                .thenReturn(new SignUpResponse(false, "Not OK", ""));
        Response response = resources.client().target("/account").request()
                .accept(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.json(ACCOUNT_SETUP)).invoke();
        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertTrue(response.hasEntity());
        SignUpResponse entity = response.readEntity(SignUpResponse.class);
        assertFalse(entity.isSuccess());
        assertEquals("{\"success\":false,\"description\":\"Not OK\"}", MAPPER.writeValueAsString(entity));
    }

    @Test
    public void signUp_InvalidRequest() throws Exception {
        when(ACCOUNT_SERVICE.register(anyObject()))
                .thenReturn(new SignUpResponse(true, "OK", "foobarre"));
        Response response = resources.client().target("/account").request()
                .accept(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.json(INVALID_ACCOUNT_SETUP)).invoke();
        assertNotNull(response);
        assertEquals(422/*Unprocessable Entity*/, response.getStatus());
        assertTrue(response.hasEntity());
        String entity = response.readEntity(String.class);
        assertEquals("{\"errors\":[\"AccountId may not be empty\"]}", entity);
    }

    @Test
    public void register_NoAuthorizationCase() throws Exception {
        when(ACCOUNT_SERVICE.registerRecordFor(anyString(), anyObject()))
                .thenReturn(new RegisteredURLResponse("http://sho.rt/gXGL01"));
        Response response = resources.client().target("/register").request()
                .accept(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.json(URL_RECORD_SETUP_FULL)).invoke();
        assertNotNull(response);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

    }

    @Test
    public void register_PositiveCase() throws Exception {
        when(ACCOUNT_SERVICE.registerRecordFor(anyString(), anyObject()))
                .thenReturn(new RegisteredURLResponse("http://sho.rt/gXGL01"));
        when(ACCOUNT_SERVICE.checkAccount(anyString(), anyString())).thenReturn(Boolean.TRUE);
        Response response = resources.client().target("/register").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .buildPost(Entity.json(URL_RECORD_SETUP_FULL)).invoke();
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertTrue(response.hasEntity());
        RegisteredURLResponse entity = response.readEntity(RegisteredURLResponse.class);
        assertEquals("http://sho.rt/gXGL01", entity.getShortUrl());
    }

    @Test
    public void register_PositiveCase_Omitted_RedirectType() throws Exception {
        when(ACCOUNT_SERVICE.registerRecordFor(anyString(), anyObject()))
                .thenReturn(new RegisteredURLResponse("http://sho.rt/gXGL01"));
        when(ACCOUNT_SERVICE.checkAccount(anyString(), anyString())).thenReturn(Boolean.TRUE);
        Response response = resources.client().target("/register").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .buildPost(Entity.json(URL_RECORD_SETUP_DFLT)).invoke();
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertTrue(response.hasEntity());
        RegisteredURLResponse entity = response.readEntity(RegisteredURLResponse.class);
        assertEquals("http://sho.rt/gXGL01", entity.getShortUrl());
    }

    @Test
    public void register_InvalidRequest() throws Exception {
        when(ACCOUNT_SERVICE.registerRecordFor(anyString(), anyObject()))
                .thenReturn(new RegisteredURLResponse("http://sho.rt/gXGL01"));
        when(ACCOUNT_SERVICE.checkAccount(anyString(), anyString())).thenReturn(Boolean.TRUE);
        Response response = resources.client().target("/register").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .buildPost(Entity.json(INVALID_ACCOUNT_SETUP)).invoke();
        assertNotNull(response);
        assertEquals(422/*Unprocessable Entity*/, response.getStatus());
    }

    @Test
    public void retrieveStatistics_StandardPositiveCase() throws Exception {
        when(ACCOUNT_SERVICE.checkAccount(anyString(), anyString())).thenReturn(Boolean.TRUE);
        when(ACCOUNT_SERVICE.getRecordsFor("good-guy")).thenReturn(new ArrayList<>());
        when(statisticsStore.requestStatistics(anyList())).thenReturn(new HashMap<String, Long>() {{
            put("https://github.com/Sulion/miles-short", 182981249232L);
            put("https://google.com", 282L);
            put("https://yandex.ru", 2387239392L);
        }});
        Response response = resources.client().target("/statistic/good-guy").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .get();
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.hasEntity());
        HashMap entity = response.readEntity(HashMap.class);
        assertEquals(3, entity.size());

    }

    @Test
    public void retrieveStatistics_EmptyMap() throws Exception {
        when(ACCOUNT_SERVICE.checkAccount(anyString(), anyString())).thenReturn(Boolean.TRUE);
        when(ACCOUNT_SERVICE.getRecordsFor("good-guy")).thenReturn(new ArrayList<>());
        when(statisticsStore.requestStatistics(anyList())).thenReturn(new HashMap<String, Long>() {{
        }});
        Response response = resources.client().target("/statistic/good-guy").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .get();
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.hasEntity());
        HashMap entity = response.readEntity(HashMap.class);
        assertEquals(0, entity.size());
    }

    @Test
    public void retrieveStatistics_SecurityViolation() throws Exception {
        when(ACCOUNT_SERVICE.checkAccount(anyString(), anyString())).thenReturn(Boolean.TRUE);
        when(ACCOUNT_SERVICE.getRecordsFor("good-guy")).thenReturn(new ArrayList<>());
        when(statisticsStore.requestStatistics(anyList())).thenReturn(new HashMap<String, Long>() {{
        }});
        Response response = resources.client().target("/statistic/another-guy").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .get();
        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

}