package ru.sulion.webapplications.resources;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by sulion on 27.08.16.
 */
@Path("{shortUrl: (?!register|account|statistic).{6}}")
public class RedirectingResource {

    @Timed
    @GET
    public Response redirectViaShortURL(@PathParam("shortUrl") String shortUrl) {
        //TODO: 1. Find actual URL as fast as it can be done (log N? 1?)
        //TODO: 2. Create task which will handle statistics update
        //TODO: 3. Redirect! Redirect! Redirect!
        return Response.temporaryRedirect(URI.create("https://google.com")).build();
    }
}
