package com.t360.game.config;

import java.util.concurrent.TimeUnit;

public class Config {
    public static int timeout = Integer.parseInt( System.getProperty("TIMEOUT","10") );
    public static TimeUnit timeoutTimeUnit = TimeUnit.valueOf( System.getProperty("TIMEOUT_TIMEUNIT","seconds").toUpperCase() );

}
