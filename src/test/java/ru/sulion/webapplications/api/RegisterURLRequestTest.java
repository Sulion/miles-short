package ru.sulion.webapplications.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by sulion on 29.08.16.
 */
public class RegisterURLRequestTest {

    public static final String TEST_INPUT = "{ \"redirectType\": 302 }";

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void testDeserialization_FailIfNoURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RegisterURLRequest urlRequest = MAPPER.readValue(TEST_INPUT, RegisterURLRequest.class);
        Validators.newValidator().validate(urlRequest);
    }

}