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
public class NetworkPlayer {
    private final static Logger logger = LoggerFactory.getLogger(NetworkPlayer.class);
    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info("Starting process per player application as: %s".formatted(args[0]) );
        if(args.length<4){
            System.out.println("""
Syntax: java -jar player-1.0-SNAPSHOT-full.jar <playerId> [initial-chat-to] [initial-message] [initial-count]
    playerId            The unique id for player from list [initiator,player1..player10]
                        In case of playerId is 'initiator' and the rest of arguments are not entered, it will try to send initial predefined messages to 'player1' in loop of 5 sequential send and receive.
    initial-chat-to     The unique id for another player from above list that you would like to send initial message to him/her
    initial-message     The initial message that you would like to send to another player
    initial-count       The count of initial sequential send and receive to the other player
""");
            System.exit(1);
        }

        Player player = new Player(args[0]);
        NetworkMessageHandler networkHandler = new NetworkMessageHandler(player.getPlayerId());

        if(player.getPlayerId().equalsIgnoreCase( "initiator" )){
            ContactProfile initiator = ContactProvider.get(player.getPlayerId());
            String response = new NetworkClient(initiator.host(), initiator.port()).sendAndWaitForResponse(
                    new RequestMessage(UUID.randomUUID().toString(),"message", "initiator", "player1", 5)
                            .toJsonString());
            logger.info("The initiator finished sending initial request: %s".formatted(response));
        }
    }
}