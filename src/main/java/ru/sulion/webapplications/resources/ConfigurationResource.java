package ru.sulion.webapplications.resources;

import ru.sulion.webapplications.api.RegisterURLRequest;
import ru.sulion.webapplications.api.RegisteredURLResponse;
import ru.sulion.webapplications.api.SignUpRequest;
import ru.sulion.webapplications.api.SignUpResponse;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sulion on 25.08.16.
 */

@Path("/")
public class ConfigurationResource {

    @PUT
    @Path("account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SignUpResponse signUp(SignUpRequest request){
        return new SignUpResponse(true, "It's not real yet", "password");
    }

    @PermitAll
    @PUT
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegisteredURLResponse register(RegisterURLRequest request) {
        return new RegisteredURLResponse("https://google.com");
    }

    @PermitAll
    @GET
    @Path("statistic/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> retrieveStatistics(@PathParam("accountId") String accountId,
                                                   @Context SecurityContext securityContext) {
        if(securityContext.getUserPrincipal().getName().equals(accountId)) {
            return new HashMap<String, Integer>() {{
                put("https://google.com/ABC", 93);
                put("https://yandex.ru/DGS", 45);
            }};
        }
        throw new ForbiddenException("You may see only your own statistics");
    }
}
