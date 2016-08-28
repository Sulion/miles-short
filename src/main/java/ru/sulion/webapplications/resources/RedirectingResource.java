package ru.sulion.webapplications.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.api.RedirectingService;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by sulion on 27.08.16.
 */
@Path("{shortUrl: (?!register|account|statistic).{6}}")
public class RedirectingResource {

    private final RedirectingService redirectingService;

    @Inject
    public RedirectingResource(RedirectingService redirectingService) {
        this.redirectingService = redirectingService;
    }

    @Timed
    @GET
    public Response redirectViaShortURL(@PathParam("shortUrl") String shortUrl) {
        Redirect redirect = redirectingService.resolveURI(shortUrl);
        if(redirect.getStatus() == Response.Status.NOT_FOUND)
            throw new NotFoundException("Sorry, but I don't know about this URL: " + shortUrl);
        return Response.status(redirect.getStatus()).location(redirect.getLocation()).build();
    }
}
