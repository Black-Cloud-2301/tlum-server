package com.kltn.individualservice.redis.specific;

import com.kltn.individualservice.redis.BaseRedisServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SpecificServiceCache extends BaseRedisServiceImpl {

    private final StudentServiceCache studentServiceCache;

    public SpecificServiceCache(RedisTemplate<String, Object> redisTemplate, StudentServiceCache studentServiceCache) {
        super(redisTemplate);
        this.studentServiceCache = studentServiceCache;
    }

//    @KafkaListener(topics = "updateCacheTopic", groupId = "group_id")
//    public void consume(ConsumerRecord<String, String> record) {
//        if (record.value().equals(studentServiceCache.getKey())) {
//            studentServiceCache.getStudentResponses();
//        }
//    }
}
