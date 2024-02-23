package com.sm.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.integration.jdbc.store.channel.ChannelMessageStoreQueryProvider;
import org.springframework.integration.jdbc.store.channel.MySqlChannelMessageStoreQueryProvider;
import org.springframework.integration.jdbc.store.channel.SqlServerChannelMessageStoreQueryProvider;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.store.ChannelMessageStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.yaml.snakeyaml.error.YAMLException;

import javax.sql.DataSource;

import java.util.Objects;

import static org.springframework.boot.jdbc.DatabaseDriver.MYSQL;
import static org.springframework.boot.jdbc.DatabaseDriver.SQLSERVER;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class MessagingConfigurationV2 {

    private final DataSource dataSource;
    @Bean(name = "routingChannel")
    public QueueChannel routingChannel() {
        return new QueueChannel(100);
    }

    // capacity 설정은 어디로?
    @Bean(name = "channel1")
    public QueueChannel channel1() {
        MessageGroupQueue messageGroupQueue = new MessageGroupQueue(channelMessageStore(), "group1");
        return new QueueChannel(messageGroupQueue);
    }

    @Bean(name = "channel2")
    public QueueChannel channel2() {
        MessageGroupQueue messageGroupQueue = new MessageGroupQueue(channelMessageStore(), "group2");
        return new QueueChannel(messageGroupQueue);
    }

    @Bean
    public ChannelMessageStore channelMessageStore() {
        JdbcChannelMessageStore messageStore = new JdbcChannelMessageStore(dataSource);
        messageStore.setChannelMessageStoreQueryProvider(new MySqlChannelMessageStoreQueryProvider());
        messageStore.afterPropertiesSet();

        return messageStore;
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        MessagingTemplate messagingTemplate = new MessagingTemplate(channel1());
        messagingTemplate.setReceiveTimeout(1000);
        messagingTemplate.setSendTimeout(1000);
        messagingTemplate.setThrowExceptionOnLateReply(true);
        return messagingTemplate;
    }
}
