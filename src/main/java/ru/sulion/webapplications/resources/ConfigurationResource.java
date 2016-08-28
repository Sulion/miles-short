package ru.sulion.webapplications.resources;

import com.google.inject.Inject;
import ru.sulion.webapplications.api.RegisterURLRequest;
import ru.sulion.webapplications.api.RegisteredURLResponse;
import ru.sulion.webapplications.api.SignUpRequest;
import ru.sulion.webapplications.api.SignUpResponse;
import ru.sulion.webapplications.core.AccountService;
import ru.sulion.webapplications.db.StatisticsStore;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Map;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final StatisticsStore statisticsStore;
    private final AccountService accountService;

    @Inject
    public ConfigurationResource(StatisticsStore statisticsStore, AccountService accountService) {
        this.statisticsStore = statisticsStore;
        this.accountService = accountService;
    }

    @PUT
    @Path("account")
    @Consumes(MediaType.APPLICATION_JSON)
    public SignUpResponse signUp(SignUpRequest request){
        return accountService.register(request);
    }

    @PermitAll
    @PUT
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public RegisteredURLResponse register(RegisterURLRequest request,
                                          @Context SecurityContext securityContext) {
        return accountService.registerRecordFor(securityContext.getUserPrincipal().getName());
    }

    @PermitAll
    @GET
    @Path("statistic/{accountId}")
    public Map<String, Integer> retrieveStatistics(@PathParam("accountId") String accountId,
                                                   @Context SecurityContext securityContext) {
        if(securityContext.getUserPrincipal().getName().equals(accountId)) {
            statisticsStore.requestStatistics(accountService.getRecordsFor(accountId));
        }
        throw new ForbiddenException("You may see only your own statistics");
    }
}
