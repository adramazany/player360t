package com.t360.game.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author: adelramezani.jd@gmail.com
 * Utility facilitate working with JSON.
 * JSON is a very good option due to transferring structured data in network.
 * extenstion: It can be easily remove by a lot of powerful existing tool.
 */
public class JsonUtil {
    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    // Convert object to json object, to be convert to string and transferred over network.
    public static JSONObject toJson(Object object) {
        Class<?> c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = null;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                logger.error("Error getting field: %s".formatted(field.getName()),e);
            }
            jsonObject.put(name, value);
        }
        return jsonObject;
    }
}
