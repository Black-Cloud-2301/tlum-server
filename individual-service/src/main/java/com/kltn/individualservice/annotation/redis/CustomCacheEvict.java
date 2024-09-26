package com.kltn.individualservice.annotation.redis;

import com.kltn.individualservice.redis.RedisKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomCacheEvict {
    String key();
    String field() default "";
    boolean allEntries() default false;
}