package ru.sulion.webapplications.api;

import java.util.List;
import java.util.Map;

public interface StatisticsStore {
    String STATISTICS_DICT = "STATS";
    boolean registerRequest(Redirect shortUrl);

    Map<String, Long> requestStatistics(List<Redirect> recordsFor);
}
