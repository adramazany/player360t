package com.t360.game.config;

import com.t360.game.model.Message;
import com.t360.game.model.RequestMessage;
import com.t360.game.util.MessageQueue;

public class MessageQueueProvider {
    public final static MessageQueue<Message> message = new MessageQueue<Message>();
    public final static MessageQueue<RequestMessage> request = new MessageQueue<RequestMessage>();
}

