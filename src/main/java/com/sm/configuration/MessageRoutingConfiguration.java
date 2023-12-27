package com.sm.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;

@Slf4j
@Configuration
public class MessageRoutingConfiguration {
//
//    @Bean
//    @ServiceActivator(inputChannel = "routingChannel")
//    public PayloadTypeRouter router() {
//        PayloadTypeRouter router = new PayloadTypeRouter();
//        router.setChannelMapping(String.class.getName(), "channel1");
//        router.setChannelMapping(Integer.class.getName(), "channel2");
//        router.afterPropertiesSet();
//        return router;
//    }

    @Bean
    @ServiceActivator(inputChannel = "routingChannel")
    public HeaderValueRouter headerValueRouter() {
        HeaderValueRouter router = new HeaderValueRouter("SID");
        router.setChannelMapping("FRUIT", "channel1");
        router.setChannelMapping("NUMBER", "channel2");
        router.afterPropertiesSet();
        return router;
    }
}
