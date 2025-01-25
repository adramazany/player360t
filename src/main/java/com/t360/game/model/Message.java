package com.t360.game.model;

import com.t360.game.util.JsonUtil;
import org.json.JSONObject;

public record Message(String messageId, String message, String from, String to, Boolean isReply) {
    public Message copyToReply(String newMessage, Boolean isReply){
        return new Message(this.messageId, newMessage, this.to, this.from, isReply);
    }
    public JSONObject toJson(){
        return JsonUtil.toJson(this);
    }
    public String toJsonString(){
        return toJson().toString();
    }
    public static boolean isMessage(JSONObject json){
        return json.has("messageId") &&
                json.has("message") &&
                json.has("from") &&
                json.has("to") &&
                json.has("isReply");
    }
    public static Message from(String jsonStr){
        JSONObject json = new JSONObject(jsonStr);
        if(isMessage(json))return from(json);
        else return null;
    }
    public static Message from(JSONObject json){
        return new Message(
                json.getString("messageId"),
                json.getString("message"),
                json.getString("from"),
                json.getString("to"),
                json.getBoolean("isReply"));
    }
}
