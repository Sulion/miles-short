package ru.sulion.webapplications.core;

import java.util.function.Consumer;

/**
 * Created by sulion on 27.08.16.
 */
public interface OverheadTaskManager {
    void execute(String shortUrl, Consumer<String> consumer);
}
