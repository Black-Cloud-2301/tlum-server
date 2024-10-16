package com.kltn.individualservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestLoadBalance {
    private Integer count = 0;

    @GetMapping
    public String test() {
        log.info("Test load balance times {}", ++count);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted", e);
        }
        return "Test load balance";
    }
}
