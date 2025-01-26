package com.t360.game.model;

/**
 * @author: adelramezani.jd@gmail.com
 * Entity of network players profile, provide host:port that a network player where listen to get messages from other players
 */
public record ContactProfile(String playerId, String host, int port) {
}
