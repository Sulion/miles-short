package ru.sulion.webapplications.db;

import ru.sulion.webapplications.api.Redirect;

public interface StatisticsStore {
    String STATISTICS_DICT = "STATS";
    boolean registerRequest(Redirect shortUrl);
}
