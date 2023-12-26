package com.sm.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.endpoint.PollingConsumer;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.integration.jdbc.store.JdbcMessageStore;
import org.springframework.integration.jdbc.store.channel.ChannelMessageStoreQueryProvider;
import org.springframework.integration.jdbc.store.channel.MySqlChannelMessageStoreQueryProvider;
import org.springframework.integration.store.ChannelMessageStore;
import org.springframework.integration.store.MessageGroupQueue;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessagingConfigurationV2 {

    private final DataSource dataSource;

    private static final int QUEUE_CAPACITY = 10;
    private static final String MESSAGE_TABLE_PREFIX = "JX_";

    // capacity 설정은 어디로?
    @Bean(name = "queueChannel")
    public QueueChannel queueChannel() {
        MessageGroupQueue messageGroupQueue = new MessageGroupQueue(channelMessageStore(), "Queue");
        return new QueueChannel(messageGroupQueue);
    }

    @Bean(name = "priorityChannel")
    public PriorityChannel priorityChannel() {
        PriorityChannel priorityChannel = new PriorityChannel();
        return priorityChannel;
    }

    @Bean
    public ChannelMessageStore channelMessageStore() {
        JdbcChannelMessageStore messageStore = new JdbcChannelMessageStore(dataSource);
//        messageStore.setTablePrefix(MESSAGE_TABLE_PREFIX); // 적용됨
        ChannelMessageStoreQueryProvider channelMessageStoreQueryProvider = new MySqlChannelMessageStoreQueryProvider();

        messageStore.setChannelMessageStoreQueryProvider(channelMessageStoreQueryProvider);
        messageStore.afterPropertiesSet();

        log.info("message Query {}", channelMessageStoreQueryProvider.getMessageQuery());
        log.info("create message Query {}", channelMessageStoreQueryProvider.getCreateMessageQuery());
        log.info("poll Query {}", channelMessageStoreQueryProvider.getPollFromGroupQuery());
        return messageStore;
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        MessagingTemplate messagingTemplate = new MessagingTemplate(queueChannel());
        messagingTemplate.setReceiveTimeout(1000);
        messagingTemplate.setSendTimeout(1000);
        messagingTemplate.setThrowExceptionOnLateReply(true);
        return messagingTemplate;
    }

    @InboundChannelAdapter(channel = "queueChannel", poller = @Poller(fixedRate = "1000"))
    public String connectQueueChannel() {
        QueueChannel queueChannel = queueChannel();
        log.info("connect1 channel adapter queue size {}", queueChannel.getQueueSize());

        return "apple";
    }


    @InboundChannelAdapter(channel = "priorityChannel", poller = @Poller(fixedRate = "500"))
    public String connectPriorityChannel() {
        log.info("connect2 channel adapter");
        return "watermelon";
    }

//    public PollingConsumer
}
