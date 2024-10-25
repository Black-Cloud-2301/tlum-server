package com.kltn.individualservice.annotation.redis.aspect;

import com.kltn.individualservice.annotation.redis.CustomCachePut;
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
public class CustomCachePutAspect {

    private final BaseRedisService baseRedisService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    public CustomCachePutAspect(BaseRedisService baseRedisService) {
        this.baseRedisService = baseRedisService;
    }

    @Around("@annotation(customCachePut)")
    public Object putCache(ProceedingJoinPoint joinPoint, CustomCachePut customCachePut) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Create evaluation context
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // Proceed with the method execution and get the result
        Object result = joinPoint.proceed();

        // Get key from enum
        String key = customCachePut.key();

        // Evaluate field expression
        String field = parser.parseExpression(customCachePut.field()).getValue(context, String.class);

        // Put the result into the cache
        baseRedisService.hashSet(key, field, result);

        return result;
    }
}