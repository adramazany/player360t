package com.t360.game.config;

import java.util.concurrent.TimeUnit;

/**
 * @author: adelramezani.jd@gmail.com
 * Provide all properties required in the system,
 * extension: loading properties from environment, files, configuration service, ...
 */
public class Config {
    // Define timeout for operations takes long time to prevent deadlocks
    public static int timeout = Integer.parseInt( System.getProperty("TIMEOUT","10") );

    // Define timeout unit ex: MILLISECONDS, SECONDS, ...
    public static TimeUnit timeoutTimeUnit = TimeUnit.valueOf( System.getProperty("TIMEOUT_TIMEUNIT","seconds").toUpperCase() );

    // Define sleep time after every peek of candidate in take method of MessageQueue
    public static long messageQueueTakeSleep = Integer.parseInt( System.getProperty("MESSAGE_QUEUE_TAKE_SLEEP","1000") );

    // Define the default host name for ContactProviders in case of game playing in network
    public static String host = System.getProperty("HOST","localhost");

    // Define the default port start range for ContactProviders
    public static int port = Integer.parseInt( System.getProperty("PORT","8000") );

    // Define the initiator player
    public static String initiator = System.getProperty("INITIATOR","initiator");

    // Define the playerIds prefix
    public static String playerIdPrefix = System.getProperty("PLAYER_ID_PREFIX","player");
}
