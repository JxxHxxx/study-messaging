package com.sm.message;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;

public class MessageProvider {


    public void test() {
        QueueChannel queueChannel = new QueueChannel();
        PublishSubscribeChannel publishSubscribeChannel = new PublishSubscribeChannel();
    }
}
