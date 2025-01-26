package com.t360.game.util;

import com.t360.game.config.Config;
import com.t360.game.model.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author: adelramezani.jd@gmail.com
 *
 */
class MessageQueueTest {
    MessageQueue<Message> messageQueue = new MessageQueue<>();
    Message expect = new Message("m1","test","player1","player2",false);

    @BeforeEach
    void setUp() {
        try {
            messageQueue.put(expect);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(value=5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void take_exists() throws ExecutionException, InterruptedException, TimeoutException {
        Message actual = messageQueue.take( (candidate) -> { return "player2".equalsIgnoreCase(candidate.to()) && !candidate.isReply(); } );
        assertEquals( expect, actual);
    }

    @Test
    @Timeout(value=15, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void take_notExists_timeOut() {
        Future<Message> actual = ExecutorService.submit(new Callable<Message>() {
            @Override
            public Message call() throws Exception {
                return messageQueue.take( (candidate) -> { return "player1".equalsIgnoreCase(candidate.to()) && !candidate.isReply(); } );
            }
        });
        assertThrows( TimeoutException.class, () -> { actual.get(Config.timeout, Config.timeoutTimeUnit);} );
    }
}