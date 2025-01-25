package com.t360.game.model;

import com.t360.game.util.JsonUtil;
import org.json.JSONObject;

public record RequestMessage(String requestId, String message, String from, String to, int count) {
    public Message copyMessage(String message){
        return new Message(requestId, message, from, to, false);
    }

    public JSONObject toJson(){
        return JsonUtil.toJson(this);
    }
    public String toJsonString(){
        return toJson().toString();
    }
    public static boolean isRequest(JSONObject json){
        return json.has("requestId") &&
                json.has("message") &&
                json.has("from") &&
                json.has("to") &&
                json.has("count");
    }
    public static RequestMessage from(String jsonStr){
        JSONObject json = new JSONObject(jsonStr);
        if(isRequest(json))return from(json);
        else return null;
    }
    public static RequestMessage from(JSONObject json){
        return new RequestMessage(
                json.getString("requestId"),
                json.getString("message"),
                json.getString("from"),
                json.getString("to"),
                json.getInt("count"));
    }
}
