package com.sm.presentation;

import com.sm.core.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/send")
    public String send() throws InterruptedException {
        log.info("request send()");
        messageService.sendMessageToChannel();
        return "송신 완료";
    }

    @GetMapping("/receive")
    public String receive() throws InterruptedException {
        log.info("request receive()");
        messageService.receiveMessage();
        return "수신 완료";
    }
}
