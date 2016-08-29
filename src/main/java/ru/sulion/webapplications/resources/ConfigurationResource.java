package ru.sulion.webapplications.resources;

import com.google.inject.Inject;
import ru.sulion.webapplications.api.RegisterURLRequest;
import ru.sulion.webapplications.api.RegisteredURLResponse;
import ru.sulion.webapplications.api.SignUpRequest;
import ru.sulion.webapplications.api.SignUpResponse;
import ru.sulion.webapplications.api.AccountService;
import ru.sulion.webapplications.api.StatisticsStore;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
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

    @POST
    @Path("account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(@Valid SignUpRequest request){
        SignUpResponse response = accountService.register(request);
        if(!response.isSuccess())
            return Response.status(Response.Status.FORBIDDEN).entity(response).build();
        return Response.ok(response).build();
    }

    @PermitAll
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(@Valid RegisterURLRequest request,
                             @Context SecurityContext securityContext) {
        RegisteredURLResponse response =  accountService.registerRecordFor(
                securityContext.getUserPrincipal().getName(), request);
        return Response.created(URI.create(response.getShortUrl())).entity(response).build();
    }

    @PermitAll
    @GET
    @Path("statistic/{accountId}")
    public Map<String, Long> retrieveStatistics(@PathParam("accountId") String accountId,
                                                   @Context SecurityContext securityContext) {
        if(securityContext.getUserPrincipal().getName().equals(accountId)) {
            return statisticsStore.requestStatistics(accountService.getRecordsFor(accountId));
        }
        throw new ForbiddenException("You may see only your own statistics");
    }
}
