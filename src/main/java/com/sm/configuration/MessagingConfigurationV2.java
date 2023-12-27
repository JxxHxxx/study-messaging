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
@Configuration
public class MessagingConfigurationV2 {

    private final DataSource dataSource;
    private static final int QUEUE_CAPACITY = 10;

    private static final String DATABASE_DRIVER_CLASS_PROPERTY_KEY = "spring.datasource.driver-class-name";
    private static final String MESSAGE_TABLE_PREFIX = "JX_";
    private final Environment env;

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

        messageStore.setChannelMessageStoreQueryProvider(selectChannelMessageStoreQueryProvider());
        messageStore.afterPropertiesSet();

        return messageStore;
    }

    private ChannelMessageStoreQueryProvider selectChannelMessageStoreQueryProvider() {
        String databaseDriverClass = env.getProperty(DATABASE_DRIVER_CLASS_PROPERTY_KEY);

        if (Objects.isNull(databaseDriverClass)) {
            log.error("{} must be not null", DATABASE_DRIVER_CLASS_PROPERTY_KEY);
            throw new YAMLException(DATABASE_DRIVER_CLASS_PROPERTY_KEY + "must be not null");
        }

        if (SQLSERVER.getDriverClassName().equals(databaseDriverClass)) {
            log.info("selected {} for ChannelMessageStoreQueryProvider ",SQLSERVER.getDriverClassName());
            return new SqlServerChannelMessageStoreQueryProvider();
        }
        log.info("selected {} for ChannelMessageStoreQueryProvider ",MYSQL.getDriverClassName());
        return new MySqlChannelMessageStoreQueryProvider();
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        MessagingTemplate messagingTemplate = new MessagingTemplate(channel1());
        messagingTemplate.setReceiveTimeout(1000);
        messagingTemplate.setSendTimeout(1000);
        messagingTemplate.setThrowExceptionOnLateReply(true);
        return messagingTemplate;
    }

    @Bean
    @ServiceActivator(inputChannel = "routingChannel")
    public PayloadTypeRouter router() {
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(String.class.getName(), "channel1");
        router.setChannelMapping(Integer.class.getName(), "channel2");
        router.afterPropertiesSet();
        return router;
    }
}
