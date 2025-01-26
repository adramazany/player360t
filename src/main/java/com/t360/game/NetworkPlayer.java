package com.t360.game;

import com.t360.game.config.Config;
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

/**
 * @author adelramezani.jd@gmail.com
 * Main class provide the network messaging platform for players
 */
public class NetworkPlayer {
    private final static Logger logger = LoggerFactory.getLogger(NetworkPlayer.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        if(args.length<1 || "--help".equalsIgnoreCase(args[0])){
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

        logger.info("Starting process per player application as: %s".formatted(args[0]) );
        Player player = new Player(args[0]);
        NetworkMessageHandler networkHandler = new NetworkMessageHandler(player.getPlayerId());

        if(player.getPlayerId().equalsIgnoreCase(Config.initiator) || args.length>1){
            ContactProfile initiator = ContactProvider.get(player.getPlayerId());

            String to = args.length>1 ? args[1] : "player1";
            String message = args.length>2 ? args[2] : "message";
            int count = args.length>3 ? Integer.parseInt(args[3]) : 5;

            String response = new NetworkClient(initiator.host(), initiator.port()).sendAndWaitForResponse(
                    new RequestMessage(UUID.randomUUID().toString(),message, initiator.playerId(), to, count)
                            .toJsonString());
            logger.info("The initiator finished sending initial request: %s".formatted(response));
        }
    }
}