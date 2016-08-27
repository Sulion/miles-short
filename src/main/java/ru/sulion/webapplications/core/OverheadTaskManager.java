package ru.sulion.webapplications.core;

import ru.sulion.webapplications.api.Redirect;

import java.util.function.Consumer;

public interface OverheadTaskManager {
    void execute(Redirect param, Consumer<Redirect> consumer);
}
