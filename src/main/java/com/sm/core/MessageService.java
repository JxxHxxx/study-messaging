package com.sm.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessagingTemplate messagingTemplate;

    public void call() {
        Message<String> stringMessage = MessageBuilder
                .withPayload("build payload")
                .build();

        messagingTemplate.send(stringMessage);
    }

}
