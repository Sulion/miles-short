package ru.sulion.webapplications.api;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.net.URI;

public class Redirect implements Serializable{
    private final Response.Status status;
    private final URI location;
    private final String shortUrl;

    public Redirect(Response.Status status, String location, String shortUrl) {
        this.status = status;
        this.location = URI.create(location);
        this.shortUrl = shortUrl;
    }

    public Response.Status getStatus() {
        return status;
    }

    public URI getLocation() {
        return location;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
