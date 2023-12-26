package com.sm.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Slf4j
@MessageEndpoint
public class SimpleMessageEndpoint {

    @ServiceActivator(inputChannel = "queueChannel")
    public void handle1(String fruit) {
        log.info("fruit name {}" , fruit);
    }


    @ServiceActivator(inputChannel = "priorityChannel")
    public void handle2(String fruit) {
        log.info("fruit name {}" , fruit);
    }
}
