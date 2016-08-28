package ru.sulion.webapplications.resources;

import com.google.inject.Inject;
import ru.sulion.webapplications.api.RegisterURLRequest;
import ru.sulion.webapplications.api.RegisteredURLResponse;
import ru.sulion.webapplications.api.SignUpRequest;
import ru.sulion.webapplications.api.SignUpResponse;
import ru.sulion.webapplications.db.StatisticsStore;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.Map;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final StatisticsStore statisticsStore;

    @Inject
    public ConfigurationResource(StatisticsStore statisticsStore) {
        this.statisticsStore = statisticsStore;
    }

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
