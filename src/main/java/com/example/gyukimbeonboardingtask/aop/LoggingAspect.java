package com.example.gyukimbeonboardingtask.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.gyukimbeonboardingtask.service.*.*(..))")
    public void logServiceMethodCall(JoinPoint joinPoint) {
        logger.info("Calling method: {} with arguments: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }
}
