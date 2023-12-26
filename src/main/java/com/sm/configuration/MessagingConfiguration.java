package com.sm.configuration;

import com.sm.core.LoggingChannelInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class MessagingConfiguration {

    private final LoggingChannelInterceptor channelInterceptor;
    private final int queueCapacity = 2;

    @Bean
    public QueueChannel queueChannel() {
        QueueChannel queueChannel = new QueueChannel(queueCapacity);
//        queueChannel.addInterceptor(channelInterceptor);
        queueChannel.afterPropertiesSet();

        return queueChannel;
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        MessagingTemplate messagingTemplate = new MessagingTemplate(queueChannel());
        messagingTemplate.setReceiveTimeout(1000);
        messagingTemplate.setSendTimeout(1000);
        messagingTemplate.setThrowExceptionOnLateReply(true);
        return messagingTemplate;
    }
}
