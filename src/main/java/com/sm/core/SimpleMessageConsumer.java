package com.sm.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@Slf4j
@MessageEndpoint
public class SimpleMessageConsumer {

    @ServiceActivator(inputChannel = "channel1")
    public void handle1(Message<String> fruit) {
        log.info("call fruit channel fruit : {}" , fruit);
    }

    @ServiceActivator(inputChannel = "channel2")
    public void handle2(Message<Integer> number) {
        log.info("call number channel number : {}" , number);
    }
}
