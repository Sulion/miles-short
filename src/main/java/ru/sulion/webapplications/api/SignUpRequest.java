package ru.sulion.webapplications.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sulion on 25.08.16.
 */
public class SignUpRequest {
    private String AccountId;

    public SignUpRequest() {
    }

    public SignUpRequest(String accountId) {
        AccountId = accountId;
    }

    @JsonProperty("AccountId")
    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }
}
