package com.t360.game.model;

import com.t360.game.util.JsonUtil;
import org.json.JSONObject;

/**
 * @author: adelramezani.jd@gmail.com
 * Entity of message transfer between players
 */
public record Message(String messageId, String message, String from, String to, Boolean isReply) {

    // Create reply to this message by changing the message and exchanging from and to
    public Message copyToReply(String newMessage, Boolean isReply){
        return new Message(this.messageId, newMessage, this.to, this.from, isReply);
    }

    // Convert this message to json string to sending over network
    public String toJsonString(){
        return JsonUtil.toJson(this).toString();
    }

    // Check the json object is message or not
    public static boolean isMessage(JSONObject json){
        return json.has( "messageId") &&
                json.has("message") &&
                json.has("from") &&
                json.has("to") &&
                json.has("isReply");
    }

    // Create message from json string
    public static Message from(String jsonStr){
        JSONObject json = new JSONObject(jsonStr);
        if(isMessage(json))return from(json);
        else return null;
    }

    // Create message from json object
    public static Message from(JSONObject json){
        return new Message(
                json.getString("messageId"),
                json.getString("message"),
                json.getString("from"),
                json.getString("to"),
                json.getBoolean("isReply"));
    }
}