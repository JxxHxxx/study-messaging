package com.sm.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessagingConfiguration {

    private final int queueCapacity = 20;
    private final int routeChannelCapacity = 100;
    private final int splitChannelCapacity = 100;

    @Qualifier(value = "RouterLoggingInterceptor")

    private final ChannelInterceptor channelInterceptor;

    @Bean(name = "channel1")
    public QueueChannel queueChannel1() {
        QueueChannel queueChannel = new QueueChannel(queueCapacity);
        queueChannel.afterPropertiesSet();

        return queueChannel;
    }

    @Bean(name = "channel2")
    public QueueChannel queueChannel2() {
        QueueChannel queueChannel = new QueueChannel(queueCapacity);
        queueChannel.afterPropertiesSet();

        return queueChannel;
    }

    @Bean(name = "routingChannel")
    public QueueChannel routingChannel() {
        QueueChannel queueChannel = new QueueChannel(routeChannelCapacity);
        queueChannel.addInterceptor(channelInterceptor);
        queueChannel.afterPropertiesSet();

        return queueChannel;
    }

    @Bean(name = "splitChannel")
    public QueueChannel splitChannel() {
        QueueChannel queueChannel = new QueueChannel(splitChannelCapacity);
        queueChannel.afterPropertiesSet();

        return queueChannel;
    }

    @Bean("inboundTaskExecutor")
    public TaskExecutor inboundTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 스레드 풀의 최소 스레드 수
        executor.setMaxPoolSize(10); // 스레드 풀의 최대 스레드 수
        executor.setQueueCapacity(400); // 큐의 최대 용량
        executor.setThreadNamePrefix("inbound-");
        return executor;
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        MessagingTemplate messagingTemplate = new MessagingTemplate(queueChannel1());
        messagingTemplate.setReceiveTimeout(1000);
        messagingTemplate.setSendTimeout(1000);
        messagingTemplate.setThrowExceptionOnLateReply(true);
        return messagingTemplate;
    }
}
