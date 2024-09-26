package com.kltn.individualservice.annotation.redis.aspect;

import com.kltn.individualservice.annotation.redis.CustomCacheEvict;
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
public class CustomCacheEvictAspect {

    private final BaseRedisService baseRedisService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    public CustomCacheEvictAspect(BaseRedisService baseRedisService) {
        this.baseRedisService = baseRedisService;
    }

    @Around("@annotation(customCacheEvict)")
    public Object evictCache(ProceedingJoinPoint joinPoint, CustomCacheEvict customCacheEvict) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Create evaluation context
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // Get key from enum
        String key = customCacheEvict.key();

        if (customCacheEvict.allEntries()) {
            baseRedisService.delete(key);
        } else {
            // Evaluate field expression
            String field = parser.parseExpression(customCacheEvict.field()).getValue(context, String.class);
            baseRedisService.delete(key, field);
        }

        // Proceed with the method execution
        return joinPoint.proceed();
    }
}