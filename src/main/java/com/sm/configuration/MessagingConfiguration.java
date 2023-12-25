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
@Configuration
public class MessagingConfiguration {

    private final LoggingChannelInterceptor channelInterceptor;

    @Bean
    public QueueChannel queueChannel() {
        QueueChannel queueChannel = new QueueChannel();
        queueChannel.addInterceptor(channelInterceptor);
        queueChannel.afterPropertiesSet();

        return queueChannel;
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        return new MessagingTemplate(queueChannel());
    }



}
