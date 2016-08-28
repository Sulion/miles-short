package ru.sulion.webapplications.core;

import ru.sulion.webapplications.api.Redirect;

import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Created by sulion on 29.08.16.
 */
public class SingleThreadTaskQueueManager implements OverheadTaskManager {

    private static final int MAX_THREADS = 1;
    private static final int TASK_CACHE_SIZE = 100;
    private final ExecutorService executor;

    public SingleThreadTaskQueueManager() {
        executor =  new ThreadPoolExecutor(MAX_THREADS, MAX_THREADS, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(TASK_CACHE_SIZE, true),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }


    @Override
    public void execute(Redirect param, Consumer<Redirect> consumer) {
        executor.submit(() -> consumer.accept(param));
    }
}
