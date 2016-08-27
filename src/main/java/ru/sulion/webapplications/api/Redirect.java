package ru.sulion.webapplications.api;

import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by sulion on 27.08.16.
 */
public class Redirect {
    private final Response.Status status;
    private final URI location;

    public Redirect(Response.Status status, String location) {
        this.status = status;
        this.location = URI.create(location);
    }

    public Response.Status getStatus() {
        return status;
    }

    public URI getLocation() {
        return location;
    }
}
