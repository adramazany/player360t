package com.t360.game.config;

import com.t360.game.model.ContactProfile;

import java.util.Hashtable;

public class ContactProvider {
    public final static Hashtable<String, ContactProfile> profiles = new Hashtable<>();

    static{
        var host = "localhost";
        var port = 8000 ;
        var defaultPlayerId = "player";
        profiles.put("initiator", new ContactProfile("initiator", host, port));
        for (int i = 1; i <= 10; i++) {
            profiles.put(defaultPlayerId+i, new ContactProfile(defaultPlayerId+i, host, port+i));
        }
    }

    public static ContactProfile get(Object key) {
        return profiles.get(key);
    }
}
