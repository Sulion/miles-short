package ru.sulion.webapplications.core.core;

import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.core.OverheadTaskManager;

import java.util.function.Consumer;

/**
 * Created by sulion on 27.08.16.
 */
public class MockTaskManager implements OverheadTaskManager{


    @Override
    public void execute(Redirect param, Consumer<Redirect> consumer) {
        consumer.accept(param);
    }
}
