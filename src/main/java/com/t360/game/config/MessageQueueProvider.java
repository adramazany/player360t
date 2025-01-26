package com.t360.game.config;

import com.t360.game.model.Message;
import com.t360.game.model.RequestMessage;
import com.t360.game.util.MessageQueue;

/**
 * @author adelramezani.jd@gmail.com
 * Provide local message queues for message and request topics
 * to enable messaging between local players
 * and provide abstract and asynchronous way of communication of network players with local player
 * extension: Replacing this with an external message queue engine
 *            will provide a simple way of having network based communication between players
 */
public class MessageQueueProvider {

    // Local message queue of message topics
    public final static MessageQueue<Message> message = new MessageQueue<Message>();

    // Local message queue of request topics
    public final static MessageQueue<RequestMessage> request = new MessageQueue<RequestMessage>();
}

