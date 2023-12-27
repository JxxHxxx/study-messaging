package com.sm.core;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.handler.MethodInvokingMessageHandler;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleMessageProducer {

    @Qualifier(value = "channel1")
    private final QueueChannel channel1;
    @Qualifier(value = "channel2")
    private final QueueChannel channel2;

    private final DataSource dataSource;

    private static final Random random = new Random();

    private static final String[] fruits = {"strawberry", "apple", "melon", "orange"};
    //여기가 뭔가 잘못됐음...
    @InboundChannelAdapter(channel = "routingChannel", poller = @Poller(fixedRate = "500"))
    public Message<?> provideToRoutingChannel() {

        log.info("routingChannel call");
        int result = random.nextInt(2);
        if (result == 1) {
            return MessageBuilder
                    .withPayload(random.nextInt(100))
                    .setHeader("SID", "NUMBER")
                    .build();
        }
        else {
            int idx = random.nextInt(4);
            return MessageBuilder
                    .withPayload(fruits[idx])
                    .setHeader("SID", "FRUIT")
                    .build();
        }
    }

//    @InboundChannelAdapter(channel = "channel1", poller = @Poller(fixedRate = "1000"))
//    public String provideToChannel1() {
//        log.info("channel1 queue size {}", channel1.getQueueSize());
//        return null;
//    }
//
//    @InboundChannelAdapter(channel = "channel2", poller = @Poller(fixedRate = "500"))
//    public Integer provideToChannel2() {
//        log.info("channel2 queue size {}", channel2.getQueueSize());
//        return null;
//    }
}
