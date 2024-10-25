package com.kltn.individualservice.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestLoadBalance {
    private Integer count = 0;
    private final MeterRegistry meterRegistry;
    private final Timer myTimer;

    public TestLoadBalance(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.myTimer = meterRegistry.timer("my_custom_requests");
    }

    @GetMapping
    public String test() {
        Timer.Sample sample = Timer.start(meterRegistry);
        log.info("Test load balance times {}", ++count);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted", e);
        }
        sample.stop(myTimer);
        return "Test load balance";
    }
}
