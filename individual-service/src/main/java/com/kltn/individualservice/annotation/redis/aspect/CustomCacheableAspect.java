package com.kltn.individualservice.annotation.redis.aspect;

import com.kltn.individualservice.annotation.redis.CustomCacheable;
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

import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class CustomCacheableAspect {

    private final BaseRedisService baseRedisService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    public CustomCacheableAspect(BaseRedisService baseRedisService) {
        this.baseRedisService = baseRedisService;
    }

    @Around("@annotation(customCacheable)")
    public Object cacheMethod(ProceedingJoinPoint joinPoint, CustomCacheable customCacheable) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Create evaluation context
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // Evaluate key and field expressions
        String key = customCacheable.key();
        String field = Objects.requireNonNull(parser.parseExpression(customCacheable.field()).getValue(context)).toString();

        // Check cache first
        List<?> cachedResult = (List<?>) baseRedisService.hashGet(key, field);
        if (cachedResult != null) {
            return cachedResult;
        }

        // Proceed with the method execution
        Object result = joinPoint.proceed();

        // Store result in cache
        baseRedisService.hashSet(key, field, result);

        return result;
    }
}