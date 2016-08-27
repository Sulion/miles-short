package ru.sulion.webapplications.api;

/**
 * Created by sulion on 27.08.16.
 */
public interface RedirectingService {
    Redirect resolveURI(String shortUrl);
}
