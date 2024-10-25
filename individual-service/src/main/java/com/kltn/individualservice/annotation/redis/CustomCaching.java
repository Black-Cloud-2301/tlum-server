package com.kltn.individualservice.annotation.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomCaching {
    CustomCacheEvict[] evict() default {};
    CustomCachePut[] put() default {};
}