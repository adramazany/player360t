package com.t360.game.model;

import com.t360.game.util.JsonUtil;
import org.json.JSONObject;

/**
 * @author adelramezani.jd@gmail.com
 * Entity of request that able to transfer between player for request,
 * to having a loop of sequential send/receive of messaging between players
 */
public record RequestMessage(String requestId, String message, String from, String to, int count) {

    /**
     * Create initial message from this request by changing the message
    */
    public Message copyMessage(String message){
        return new Message(requestId, message, from, to, false);
    }

    /**
     * Convert this request to json string to sending over network
    */
    public String toJsonString(){
        return JsonUtil.toJson(this).toString();
    }

    /**
     * Check the json object is request or not
    */
    public static boolean isRequest(JSONObject json){
        return json.has("requestId") &&
                json.has("message") &&
                json.has("from") &&
                json.has("to") &&
                json.has("count");
    }

    /**
     * Create request from json string
    */
    public static RequestMessage from(String jsonStr){
        JSONObject json = new JSONObject(jsonStr);
        if(isRequest(json))return from(json);
        else return null;
    }

    /**
     * Create request from json object
    */
    public static RequestMessage from(JSONObject json){
        return new RequestMessage(
                json.getString("requestId"),
                json.getString("message"),
                json.getString("from"),
                json.getString("to"),
                json.getInt("count"));
    }
}