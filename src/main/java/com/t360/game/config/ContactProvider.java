package com.t360.game.config;

import com.t360.game.model.ContactProfile;

import java.util.Hashtable;

/**
 * @author: adelramezani.jd@gmail.com
 * Provide all network player profiles, including there URIs to enable us to communicate wit them over network
 * extension: load these information from configuration server, type of message, ...
 */
public class ContactProvider {
    public final static Hashtable<String, ContactProfile> profiles = new Hashtable<>();

    // Load sample players: initiator@localhost:8000, player1@localhost:8001-player10@localhost:8010
    static{
        profiles.put(Config.initiator, new ContactProfile(Config.initiator, Config.host, Config.port));
        for (int i = 1; i <= 10; i++) {
            profiles.put(Config.playerIdPrefix+i, new ContactProfile(Config.playerIdPrefix+i, Config.host, Config.port+i));
        }
    }

    public static ContactProfile get(String playerId) {
        return profiles.get(playerId);
    }
}
