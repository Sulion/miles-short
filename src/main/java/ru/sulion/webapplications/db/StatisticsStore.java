package ru.sulion.webapplications.db;

import ru.sulion.webapplications.api.Redirect;

import java.util.List;
import java.util.Map;

public interface StatisticsStore {
    String STATISTICS_DICT = "STATS";
    boolean registerRequest(Redirect shortUrl);

    Map<String, Long> requestStatistics(List<String> request);
}
