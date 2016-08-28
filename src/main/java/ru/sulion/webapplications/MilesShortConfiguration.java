package ru.sulion.webapplications;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class MilesShortConfiguration extends Configuration {
    @NotEmpty
    private String dbFileName;
    @NotEmpty
    private String HTTPUrl;

    @JsonProperty("dbFileName")
    public String getDbFileName() {
        return dbFileName;
    }

    @JsonProperty("dbFileName")
    public void setDbFileName(String dbFileName) {
        this.dbFileName = dbFileName;
    }

    @JsonProperty("httpUrl")
    public String getHTTPUrl() {
        return HTTPUrl;
    }

    @JsonProperty("httpUrl")
    public void setHTTPUrl(String HTTPUrl) {
        this.HTTPUrl = HTTPUrl;
    }
}
