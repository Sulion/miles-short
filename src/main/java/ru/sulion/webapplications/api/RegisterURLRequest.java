package ru.sulion.webapplications.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Response;

/**
 * Created by sulion on 25.08.16.
 */
public class RegisterURLRequest {
    //TODO: url is mandatory
    //TODO: keep redirectType as int
    private String url;
    private Response.Status redirectType;

    public RegisterURLRequest() {
    }

    public RegisterURLRequest(String url, Response.Status redirectType) {
        this.url = url;
        this.redirectType = redirectType;
    }

    @JsonProperty
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty
    public Response.Status getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Response.Status redirectType) {
        this.redirectType = redirectType;
    }
}
