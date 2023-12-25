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

    @GetMapping("/call")
    public String call() {
        log.info("call MessageController ");
        messageService.call();
        return "호출 완료";
    }
}
