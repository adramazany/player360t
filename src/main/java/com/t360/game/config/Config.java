package com.t360.game.config;

import java.util.concurrent.TimeUnit;

public class Config {
    public static int timeout = Integer.parseInt( System.getProperty("TIMEOUT","10") );
    public static TimeUnit timeoutTimeUnit = TimeUnit.valueOf( System.getProperty("TIMEOUT_TIMEUNIT","seconds").toUpperCase() );

    public static long messageQueueTakeSleep = Integer.parseInt( System.getProperty("MESSAGE_QUEUE_TAKE_SLEEP","1000") );
}
