package com.t360.game;

import com.t360.game.model.RequestMessage;
import com.t360.game.config.MessageQueueProvider;
import com.t360.game.service.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class SingleProcessPlayers {
    private final static Logger logger = LoggerFactory.getLogger(SingleProcessPlayers.class);
    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting Single Process Players application ...");

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");

        MessageQueueProvider.request.put(new RequestMessage(
                UUID.randomUUID().toString(),"message", "player1", "player2", 5
        ));

    }
}