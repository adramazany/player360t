package com.t360.game.service;

import com.t360.game.config.ContactProvider;
import com.t360.game.config.MessageQueueProvider;
import com.t360.game.model.ContactProfile;
import com.t360.game.model.Message;
import com.t360.game.model.RequestMessage;
import com.t360.game.util.ExecutorService;
import com.t360.game.util.NetworkClient;
import com.t360.game.util.NetworkServer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * @author adelramezani.jd@gmail.com
 * Service provide infrastructure of messaging between network players,
 *      and listening to messages.
 * By running a network server to get message from network players.
 * And handling network messages receive in string format by detection of its type (message/request),
 *      and putting it over local messaging infrastructure,
 *      and taking and replying over network.
 * And handling local messages waiting to transferred over network,
 *      and returning the response into the local messaging queue
 */
public class NetworkMessageHandler {
    private final static Logger logger = LoggerFactory.getLogger(NetworkMessageHandler.class);
    private final String playerId;
    private final ContactProfile playerProfile;

    /**
     *  Running a NetworkServer and forwarding received messages to handleReceivedMessageAndReply,
     *  And starting listener to network messages,
     *  And starting the handleSendMessageAndWaitForResponse to handle local messages
     */
    public NetworkMessageHandler(String playerId){
        this.playerId = playerId;
        this.playerProfile = ContactProvider.get(playerId);
        logger.info("The network handler started for: %s".formatted(playerProfile));

        NetworkServer server = new NetworkServer(playerProfile.port(), this::handleReceivedMessageAndReply);
        ExecutorService.execute(server::listen);
        ExecutorService.execute(this::handleSendMessageAndWaitForResponse);
    }

    /**
     * Handling string network messages by detection of its type (message/request),
     *      and putting it over local messaging infrastructure,
     *      and taking and replying over network.
     */
    public String handleReceivedMessageAndReply(String networkMessage) {
        JSONObject json = new JSONObject(networkMessage);

        if (Message.isMessage(json)) {
            Message message = Message.from(json);
            try {
                MessageQueueProvider.message.put(message);
            } catch (InterruptedException e) {
                logger.error( "Error in sending received message to player!", e );
                return "500-Internal server error";
            }
            return MessageQueueProvider.message.take(
                    (candidate) -> { return
                            candidate.messageId().equalsIgnoreCase(message.messageId()) &&
                                    candidate.isReply() &&
                                    message.from().equalsIgnoreCase(candidate.to()); } ).toJsonString();

        } else if (RequestMessage.isRequest(json)) {
            RequestMessage request = RequestMessage.from(json);
            try {
                MessageQueueProvider.request.put(request);
                return "200-OK";
            } catch (InterruptedException e) {
                logger.error( "Error in sending received request to player!", e );
                return "500-Internal server error";
            }
        }
        return "400-Bad request";
    }

    /**
     * Handling local messages waiting to transferred over network,
     *      and returning the response into the local messaging queue
     */
    public void handleSendMessageAndWaitForResponse(){
        logger.info("The process of listen to internal message and sending to the network and wait for response started for: %s".formatted(getPlayerId()));
        while(true) {
            Message message = MessageQueueProvider.message.take((candidate) -> {
                return getPlayerId().equalsIgnoreCase(candidate.from()) &&
                        ContactProvider.get(candidate.to())!=null &&
                        !candidate.isReply();
            });

            ContactProfile toProfile = ContactProvider.get(message.to());
            try {
                String responseStr = getNetworkClient(toProfile.host(), toProfile.port()).sendAndWaitForResponse( message.toJsonString());
                Message response = Message.from(responseStr);
                MessageQueueProvider.message.put(response);
                logger.debug("The message replied by: %s".formatted(response));
            } catch (IOException e) {
                logger.error("Error in calling sendAndWaitForResponse for: %s".formatted(message),e);
            } catch (InterruptedException e) {
                logger.error("It was not able to put reply message in the queue",e);
            }
        }
    }
    public NetworkClient getNetworkClient(String host, int port) throws IOException {
        return new NetworkClient(host, port);
    }

    public String getPlayerId() {
        return playerId;
    }
}