package com.t360.game.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author adelramezani.jd@gmail.com
 * Utility for facilitate multi-threading
 */
public class ExecutorService {
    private final static java.util.concurrent.ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Execute a runnable task in a new thread
    */
    public static void execute(Runnable command) {
        executorService.execute(command);
    }

    /**
     * Execute a callable task in a new thread and return the result
    */
    public static <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }
}