package com.sm.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@Slf4j
@MessageEndpoint
public class SimpleMessageConsumer {

    @ServiceActivator(inputChannel = "channel1")
    public void handle1(String fruit) {
        log.info("fruit name {}" , fruit);
    }


    @ServiceActivator(inputChannel = "channel2")
    public void handle2(Integer number) {
        log.info("number name {}" , number);
    }
}
