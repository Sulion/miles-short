package ru.sulion.webapplications.db;

import ru.sulion.webapplications.api.Redirect;

/**
 * Created by sulion on 27.08.16.
 */
public interface RedirectDictionary {
    String DICT_NAME = "DICT_NAME";

    Redirect find(String shortUrl);
}
