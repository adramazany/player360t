package com.t360.game.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorService {
    private final static java.util.concurrent.ExecutorService executorService = Executors.newCachedThreadPool();

    public static void execute(Runnable command) {
        executorService.execute(command);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }
}
