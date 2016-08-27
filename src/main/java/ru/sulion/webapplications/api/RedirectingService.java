package ru.sulion.webapplications.api;

/**
 * Created by sulion on 27.08.16.
 */
public interface RedirectingService {
    //TODO: 1. Find actual URL as fast as it can be done (log N? 1?)
    //TODO: 2. Create task which will handle statistics update
    //TODO: 3. Redirect! Redirect! Redirect!
    Redirect resolveURI(String shortUrl);
}
