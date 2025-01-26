package com.t360.game.service;

import com.t360.game.config.MessageQueueProvider;
import com.t360.game.model.Message;
import com.t360.game.model.RequestMessage;
import com.t360.game.util.ExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author adelramezani.jd@gmail.com
 *
 */
class PlayerTest {


    @BeforeEach
    void setUp() {
        MessageQueueProvider.message.clear();
    }

    @Test
    @Timeout(value=5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void sendMessageAndWaitForResponse() throws ExecutionException, InterruptedException {
        Message message= new Message("m1", "test 0","player1","player2",false);
        Message expected= new Message("m1","test 0 1","player2","player1",true);

        Player player1 = new Player("player1");

        Future<Message> actual = ExecutorService.submit(() ->
            { return player1.sendMessageAndWaitForResponse(message); });

        MessageQueueProvider.message.take(candidate -> { return true; });
        MessageQueueProvider.message.put(expected);

        assertEquals(expected, actual.get());
    }

    @Timeout(value=15, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void receiveRequestAndRepeatSending() throws InterruptedException {
        RequestMessage request= new RequestMessage("r1", "test","player1","player2",2);
        Message expected1= new Message("r1", "test 0","player1","player2",false);
        Message expected2= new Message("r1","test 0 1 1","player1","player2",false);

        Player player1 = new Player("player1");

        MessageQueueProvider.request.put(request);

        Message actual1 = MessageQueueProvider.message.take(candidate -> { return !candidate.isReply(); });
        MessageQueueProvider.message.put(actual1.copyToReply(actual1.message()+" 1", true));
        Message actual2 = MessageQueueProvider.message.take(candidate -> {  return !candidate.isReply(); });

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void receiveMessageAndReply() throws InterruptedException {
        Message message= new Message("m1", "test 0","player2","player1",false);
        Message expected= new Message("m1","test 0 1","player1","player2",true);

        Player player1 = new Player("player1");

        MessageQueueProvider.message.put(message);

        Message actual = MessageQueueProvider.message.take(candidate -> { return candidate.isReply(); });

        assertEquals(expected, actual);
    }
}