package com.sm.core;

import com.sm.infra.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleMessageProducer {

    private static final Random random = new Random();
    private static Long messageId = 0l;

    @Value("${message.produce.size}")
    private int produceSize;

    @InboundChannelAdapter(channel = "splitChannel",
            poller = @Poller(fixedDelay = "${message.produce.polling.interval}",
                             taskExecutor = "inboundTaskExecutor"))
    public List<Message<?>> produce1() {
        Integer producerId = 2;
        List<String> messageIdContainer = new ArrayList<>();

        List<Message<?>> messages = new ArrayList<>();
        for (int i = 0; i < produceSize; i++) {
            Message<?> message = produceMessage(producerId);
            messages.add(message);
            messageIdContainer.add(getMessageId(message));
        }
        log.info("[PRODUCER-{}][TO:ROUTING_CHANNEL][MSG ID:{}]", producerId, messageIdContainer);

        return messages;
    }

    @InboundChannelAdapter(channel = "splitChannel", poller = @Poller(fixedDelay = "${message.produce.polling.interval}"))
    public List<Message<?>> produce2() {
        Integer producerId = 2;
        List<String> messageIdContainer = new ArrayList<>();

        List<Message<?>> messages = new ArrayList<>();
        for (int i = 0; i < produceSize; i++) {
            Message<?> message = produceMessage(producerId);
            messages.add(message);
            messageIdContainer.add(getMessageId(message));
        }
        log.info("[PRODUCER-{}][TO:ROUTING_CHANNEL][MSG ID:{}]", producerId, messageIdContainer);
        return messages;
    }

    private static String getMessageId(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        return String.valueOf(headers.get("MSGID"));
    }

    private Message<? extends Serializable> produceMessage(int producerId) {
        int result = random.nextInt(2);
        if (result == 1) {
            return MessageBuilder
                    .withPayload(random.nextInt(100))
                    .setHeader("MSGID", messageId++)
                    .setHeader("PDID", producerId)
                    .setHeader("SID", "NUMBER")
                    .build();
        }
        else {
            int idx = random.nextInt(4);
            return MessageBuilder
                    .withPayload(Store.FRUITS[idx])
                    .setHeader("MSGID", messageId++)
                    .setHeader("PDID", producerId)
                    .setHeader("SID", "FRUIT")
                    .build();
        }
    }
}
