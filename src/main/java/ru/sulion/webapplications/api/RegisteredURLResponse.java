package ru.sulion.webapplications.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sulion on 25.08.16.
 */
public class RegisteredURLResponse {
    private final String shortUrl;

    public RegisteredURLResponse() {
        shortUrl = null;
    }

    public RegisteredURLResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @JsonProperty
    public String getShortUrl() {
        return shortUrl;
    }
}
