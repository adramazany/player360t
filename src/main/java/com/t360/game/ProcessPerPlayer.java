package com.t360.game;

import com.t360.game.config.ContactProvider;
import com.t360.game.model.ContactProfile;
import com.t360.game.model.RequestMessage;
import com.t360.game.service.NetworkMessageHandler;
import com.t360.game.service.Player;
import com.t360.game.util.NetworkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ProcessPerPlayer {
    private final static Logger logger = LoggerFactory.getLogger(ProcessPerPlayer.class);
    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info("Starting process per player application as: %s".formatted(args[0]) );

        Player player = new Player(args[0]);
        NetworkMessageHandler networkHandler = new NetworkMessageHandler(player.getPlayerId());

        if(player.getPlayerId().equalsIgnoreCase( "initiator" )){
            ContactProfile initiator = ContactProvider.get(player.getPlayerId());
            String response = NetworkClient.sendAndWaitForResponse(
                    new RequestMessage(UUID.randomUUID().toString(),"message", "initiator", "player1", 5)
                            .toJsonString(), initiator.host(), initiator.port());
            logger.info("The initiator finished sending initial request: %s".formatted(response));
        }
    }
}