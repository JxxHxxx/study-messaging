package com.sm.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessagingTemplate messagingTemplate;
    @Qualifier(value = "channel1")
    private final QueueChannel channel1;

    // 채널로 메시지를 넣는다. Producer
    public void sendMessageToChannel() throws InterruptedException {
        
        Message<String> stringMessage = MessageBuilder
                .withPayload(UUID.randomUUID().toString())
                .build();
        messagingTemplate.send(stringMessage);
        log.info("큐 채널에 메시지를 넣습니다. payload {}", stringMessage.getPayload());
        log.info("현재 잔여 큐 {}", channel1.getRemainingCapacity());
    }
    // 메시지를 목적지로 보낸다. Consumer
    public void receiveMessage() throws InterruptedException {
        Message<?> receivedMessage = messagingTemplate.receive();
        Thread.sleep(2000);
        log.info("큐 채널에서 메시지를 가져옵니다. payload {}", receivedMessage.getPayload());
        log.info("현재 잔여 큐 {}", channel1.getRemainingCapacity());
    }
}
