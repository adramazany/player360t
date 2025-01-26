package com.t360.game.util;

import com.t360.game.model.Message;
import com.t360.game.model.RequestMessage;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: adelramezani.jd@gmail.com
 *
 */
class JsonUtilTest {

    @Test
    void toJson_message() {
        JSONObject expected = new JSONObject("{\"messageId\":\"m1\",\"from\":\"player1\",\"to\":\"player2\",\"message\":\"test\",\"isReply\":false}");
        Message message = new Message("m1","test","player1","player2",false);
        assertTrue(expected.similar(JsonUtil.toJson(message)));
    }

    @Test
    void toJson_request() {
        JSONObject expected = new JSONObject("{\"requestId\":\"r1\",\"count\":5,\"from\":\"player1\",\"to\":\"player2\",\"message\":\"test\"}");
        RequestMessage request = new RequestMessage("r1","test","player1","player2",5);
        assertTrue(expected.similar(JsonUtil.toJson(request)));
    }
}