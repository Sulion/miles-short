package ru.sulion.webapplications.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sulion on 25.08.16.
 */
public class SignUpResponse {
    //TODO: default password to an empty string in case of unsuccessful registration
    private final boolean success;
    private final String description;
    private final String password;

    public SignUpResponse(boolean success, String description, String password) {
        this.success = success;
        this.description = description;
        this.password = password;
    }

    @JsonProperty
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }
}
