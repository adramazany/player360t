package com.t360.game.util;

import java.util.concurrent.Executors;

public class ExecutorService {
    private final static java.util.concurrent.ExecutorService executorService = Executors.newCachedThreadPool();

    public static void execute(Runnable command) {
        executorService.execute(command);
    }
}
