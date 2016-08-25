package ru.sulion.webapplications.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sulion on 25.08.16.
 */
public class RegisterURLRequest {
    private String url;
    private int redirectType;

    public RegisterURLRequest() {
    }

    public RegisterURLRequest(String url, int redirectType) {
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
    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }
}
