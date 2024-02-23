package com.sm.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.Message;

import java.util.List;

@Configuration
public class SimpleRouter {
    @Bean
    @Router(inputChannel = "routingChannel")
    public HeaderValueRouter headerValueRouter() {
        HeaderValueRouter router = new HeaderValueRouter("SID");
        router.setChannelMapping("FRUIT", "channel1");
        router.setChannelMapping("NUMBER", "channel2");
        router.afterPropertiesSet();
        return router;
    }

    @Splitter(inputChannel = "splitChannel", outputChannel = "routingChannel")
    public List<Message<?>> splitMessages(List<Message<?>> messages) {
        return messages;
    }
}
