package ru.sulion.webapplications.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by sulion on 25.08.16.
 */
public class SignUpRequest {
    @NotEmpty
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
