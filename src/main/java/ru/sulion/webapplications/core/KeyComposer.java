package ru.sulion.webapplications.core;

/**
 * Created by sulion on 27.08.16.
 */
public class KeyComposer {



    public String toStatsKey(String shortUrl, String fullUrl) {
        return shortUrl + fullUrl;
    }

    public String removePrefix(String statsKey) {
        return statsKey.substring(6);
    }


}
