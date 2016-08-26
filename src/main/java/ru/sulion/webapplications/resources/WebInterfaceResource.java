package ru.sulion.webapplications.resources;

import com.codahale.metrics.annotation.Timed;
import ru.sulion.webapplications.api.RegisterURLRequest;
import ru.sulion.webapplications.api.RegisteredURLResponse;
import ru.sulion.webapplications.api.SignUpRequest;
import ru.sulion.webapplications.api.SignUpResponse;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sulion on 25.08.16.
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class WebInterfaceResource {

    @PUT
    @Path("account")
    @Consumes(MediaType.APPLICATION_JSON)
    public SignUpResponse signUp(SignUpRequest request){
        return new SignUpResponse(true, "It's not real yet", "password");
    }

    @PermitAll
    @PUT
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public RegisteredURLResponse register(RegisterURLRequest request) {
        return new RegisteredURLResponse("https://google.com");
    }

    @PermitAll
    @GET
    @Path("statistic/{accountId}")
    public Map<String, Integer> retrieveStatistics(@PathParam("accountId") String accountId) {
        return new HashMap<String, Integer>(){{
            put("https://google.com/ABC", 93);
            put("https://yandex.ru/DGS", 45);
        }};
    }

    @Timed
    @GET
    @Path("{shortUrl: (?!register|account|statistic).*}")
    public SignUpRequest redirectViaShortURL(@PathParam("shortUrl") String shortUrl) {
        return new SignUpRequest(shortUrl);
    }
}
