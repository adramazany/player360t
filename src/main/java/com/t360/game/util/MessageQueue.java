package com.t360.game.util;

import com.t360.game.config.Config;
import com.t360.game.config.MessageQueueProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class MessageQueue<T> {
    private final static Logger logger = LoggerFactory.getLogger(MessageQueueProvider.class);
    private final BlockingQueue<T> blockingQueue = new LinkedBlockingQueue<T>();

    public void put(T t) throws InterruptedException {
        blockingQueue.put(t);
    }

    public T take(Function<T, Boolean> valiate){
        while(true){
            T message;
            while((message = blockingQueue.peek()) == null) {
                // Wait for a short interval (e.g., using Thread.sleep())
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
}