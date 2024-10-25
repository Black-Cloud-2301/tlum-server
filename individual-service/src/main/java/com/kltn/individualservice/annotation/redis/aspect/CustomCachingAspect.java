package com.kltn.individualservice.annotation.redis.aspect;

import com.kltn.individualservice.annotation.redis.CustomCacheEvict;
import com.kltn.individualservice.annotation.redis.CustomCachePut;
import com.kltn.individualservice.annotation.redis.CustomCaching;
import com.kltn.individualservice.redis.BaseRedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomCachingAspect {

    private final BaseRedisService baseRedisService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    public CustomCachingAspect(BaseRedisService baseRedisService) {
        this.baseRedisService = baseRedisService;
    }

    @Around("@annotation(customCaching)")
    public Object handleCustomCaching(ProceedingJoinPoint joinPoint, CustomCaching customCaching) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Create evaluation context
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // Handle evict annotations
        for (CustomCacheEvict evict : customCaching.evict()) {
            String key = evict.key();
            if (evict.allEntries()) {
                baseRedisService.delete(key);
            } else {
                String field = parser.parseExpression(evict.field()).getValue(context, String.class);
                baseRedisService.delete(key, field);
            }
        }

        // Proceed with the method execution and get the result
        Object result = joinPoint.proceed();

        // Handle put annotations
        for (CustomCachePut put : customCaching.put()) {
            String key = put.key();
            String field = parser.parseExpression(put.field()).getValue(context, String.class);
            baseRedisService.hashSet(key, field, result);
        }

        return result;
    }
}