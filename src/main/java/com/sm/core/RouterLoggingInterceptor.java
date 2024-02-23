package com.sm.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "RouterLoggingInterceptor")
@RequiredArgsConstructor
public class RouterLoggingInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        MessageHeaders headers = message.getHeaders();
        String sid = String.valueOf(headers.get("SID"));
        String pdid = String.valueOf(headers.get("PDID"));
        log.info("[ROUTING_CHANNEL][RECEIVE][FROM:PRODUCER-{}][SEND][TO:{}]", pdid, sid);
        ChannelInterceptor.super.postSend(message, channel, sent);
    }
}
