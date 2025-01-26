package com.t360.game.service;

import com.t360.game.config.MessageQueueProvider;
import com.t360.game.config.ReplyMessageProvider;
import com.t360.game.model.Message;
import com.t360.game.model.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.t360.game.util.ExecutorService;

/**
 * @author adelramezani.jd@gmail.com
 * Service provide infrastructure of messaging between local players,
 * By running a network server to get message from network players.
 * Handling local messages,
 *      by listening messages on local message queue,
 *      and replying in the same way.
 * And handling local request of repeating a sequential send/receive messaging between local players
 *      and returning the response into the local messaging queue
 */
public class Player{
    private final static Logger logger = LoggerFactory.getLogger(Player.class);
    private final String playerId;

    /**
     *  Starting listener for getting local messages and reply to them,
     *  And starting listener for getting local request to start a loop of sequential send/receive messages between local players,
     */
    public Player(String playerId){
        this.playerId = playerId;
        ExecutorService.execute(this::receiveMessageAndReply);
        ExecutorService.execute(this::receiveRequestAndRepeatSending);
        logger.info("Player with id: %s created.".formatted(playerId));
    }

    /**
     * Handling local request,
     *      by listening request on local message queue,
     *      and start a loop of sequential send/receive messages between local players.
     */
    public void receiveRequestAndRepeatSending(){
        while(true) {
            RequestMessage request = MessageQueueProvider.request.take((candidate) -> {
                return playerId.equalsIgnoreCase(candidate.from());
            });
            Message message = request.copyMessage(request.message() + " 0");
            try {
                for (int i = 0; i < request.count(); i++) {
                    logger.debug("Loop %d".formatted(i));
                    Message response = sendMessageAndWaitForResponse(message);
                    message = response.copyToReply(ReplyMessageProvider.parseAndReply(response.message()), false);
                }
            } catch (InterruptedException ex) {
                logger.error("The loop of request/reply broken caused by exception in sendMessageAndWaitForResponse!",ex);
            }
        }
    }

    /**
     * Sending message to local player by putting it in the message queue,
     * And waiting to its response to return.
     */
    public Message sendMessageAndWaitForResponse(Message message) throws InterruptedException {
        logger.debug("This message will send and wait for response: %s".formatted(message));
        try {
            MessageQueueProvider.message.put(message);
        }catch(InterruptedException ex){
            logger.error("It was not able to put message in MessageQueue! "+ex.getMessage());
            throw ex;
        }
        return MessageQueueProvider.message.take(
                (candidate) -> { return
                candidate.messageId().equalsIgnoreCase(message.messageId()) &&
                candidate.isReply() &&
                playerId.equalsIgnoreCase(candidate.to()); } );
    }

    /**
     * Handling local messages,
     *      by listening messages on local message queue,
     *      and replying in the same way.
     */
    public void receiveMessageAndReply(){
        logger.info("The process of receiving message and reply started for: %s".formatted(playerId));
        while(true) {
            Message message = MessageQueueProvider.message.take((candidate) -> {
                return playerId.equalsIgnoreCase(candidate.to()) && !candidate.isReply();
            });
            Message reply = message.copyToReply( ReplyMessageProvider.parseAndReply( message.message() ), true );
            try{
                MessageQueueProvider.message.put(reply);
                logger.debug("The message replied by: %s".formatted(reply));
            }catch(InterruptedException ex){
                logger.error("It was not able to put reply message in the queue: %s".formatted(reply),ex);
            }
        }
    }

    public String getPlayerId() {
        return playerId;
    }
}