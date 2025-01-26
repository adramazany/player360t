package com.t360.game.service;

import com.t360.game.config.MessageQueueProvider;
import com.t360.game.model.Message;
import com.t360.game.util.ExecutorService;
import com.t360.game.util.JsonUtil;
import com.t360.game.util.NetworkClient;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NetworkMessageHandlerTest {

    String playerId = "player1";
    Player player1 = new Player(playerId);

    @Test
    @Timeout(value=15, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void handleReceivedMessageAndReply_isMessage() {
        String message = "{\"messageId\":\"m1\",\"from\":\"player2\",\"to\":\"player1\",\"message\":\"test 0\",\"isReply\":false}";
        String expected = "{\"messageId\":\"m1\",\"from\":\"player1\",\"to\":\"player2\",\"message\":\"test 0 1\",\"isReply\":true}";

        NetworkMessageHandler networkHandler = new NetworkMessageHandler(playerId);
        String actual = networkHandler.handleReceivedMessageAndReply(message);

        assertEquals(expected, actual);
    }

    @Test
    @Timeout(value=15, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void handleReceivedMessageAndReply_isRequest() {
        String request = "{\"requestId\":\"r1\",\"count\":1,\"from\":\"player1\",\"to\":\"player2\",\"message\":\"test\"}";
        String expected = "200-OK";

        NetworkMessageHandler networkHandler = new NetworkMessageHandler(playerId);
        String actual = networkHandler.handleReceivedMessageAndReply(request);

        assertEquals(expected, actual);
    }

    @Test
    @Timeout(value=15, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void handleSendMessageAndWaitForResponse() throws Exception {
        Message message = new Message("m1", "test 0","player1","player2",false);
        Message expected = new Message("m1", "test 0 1","player2","player1",true);

        NetworkClient mockClient = mock(NetworkClient.class);
        when(mockClient.sendAndWaitForResponse(any(String.class))).thenReturn(JsonUtil.toJson(expected).toString());

        NetworkMessageHandler mockNetworkHandler = mock(NetworkMessageHandler.class);
        doCallRealMethod().when(mockNetworkHandler).handleSendMessageAndWaitForResponse();
        when(mockNetworkHandler.getNetworkClient(anyString(), anyInt())).thenReturn(mockClient);
        when(mockNetworkHandler.getPlayerId()).thenReturn(playerId);

        ExecutorService.execute(() -> mockNetworkHandler.handleSendMessageAndWaitForResponse());

        Message actual = player1.sendMessageAndWaitForResponse(message);

        assertEquals(expected, actual);
    }
}