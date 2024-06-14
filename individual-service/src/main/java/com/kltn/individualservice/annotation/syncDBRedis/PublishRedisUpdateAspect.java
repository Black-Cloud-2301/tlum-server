package com.kltn.individualservice.annotation.syncDBRedis;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PublishRedisUpdateAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @AfterReturning("@annotation(com.kltn.individualservice.annotation.syncDBRedis.PublishRedisUpdate) && @annotation(anno)")
    public void afterReturning(PublishRedisUpdate anno) {
        String[] values = anno.value();
        for (String value : values) {
            kafkaTemplate.send("updateCacheTopic", value);
        }
    }
}