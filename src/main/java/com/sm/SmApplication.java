package com.sm;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SmApplication {


    public static void main(String[] args) {
        SpringApplication.run(SmApplication.class, args);
    }

}
