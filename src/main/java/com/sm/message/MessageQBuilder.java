package com.sm.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import static org.springframework.integration.IntegrationMessageHeaderAccessor.DUPLICATE_MESSAGE;

@Slf4j
public class MessageQBuilder {
    public static Message<MessageQ> create(MessageQ messageQ) {
        Message<MessageQ> message = MessageBuilder
                .withPayload(messageQ)
                .setHeader("SID", "ORDER-001")
                .build();

        return message;
    }

    public static Message<MessageQ> createV2(MessageQ messageQ) {

        MessageHeaderAccessor messageHeaderAccessor = new MessageHeaderAccessor();
        Message<MessageQ> message = MessageBuilder
                .withPayload(messageQ)

                .build();

        return message;
    }


    public static void main(String[] args) {
        MessageQ messageQ = new MessageQ("hi");
        Message<MessageQ> message = MessageQBuilder.create(messageQ);

        log.info("message header {} payload {}", message.getHeaders(), message.getPayload());
    }

}
