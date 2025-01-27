package com.t360.game.util;

import com.t360.game.config.Config;
import com.t360.game.config.MessageQueueProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * @author adelramezani.jd@gmail.com
 * Utility simplify creating new topics for local messaging
 */
public class MessageQueue<T> {
    private final static Logger logger = LoggerFactory.getLogger(MessageQueueProvider.class);

    /**
     * Define a linked blocking queue (perfect for multi-threading platform) for every topic
    */
    private final BlockingQueue<T> blockingQueue = new LinkedBlockingQueue<T>();

    /**
     * Put a message of specific topic into its queue
    */
    public void put(T t) throws InterruptedException {
        blockingQueue.put(t);
    }

    /**
     * Waiting for a specific message of define topic that pass the validation rule will define by caller
    */
    public T take(Function<T, Boolean> valiate){
        while(true){
            T message;
            while((message = blockingQueue.peek()) == null) {
                try{
                    Thread.sleep(Config.messageQueueTakeSleep);
                }catch(Exception ex){
                    logger.error(ex.getMessage());
                }
            }
            if(valiate.apply(message)){
                logger.info("A request for initiating dialog received: %s".formatted(message.toString()));
                blockingQueue.remove(message);
                return message;
            }
        }
    }

    /**
     * Clear all existing messages in queue
    */
    public void clear() {
        blockingQueue.clear();
    }
}