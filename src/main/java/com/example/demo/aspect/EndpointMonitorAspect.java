package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EndpointMonitorAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointMonitorAspect.class);

    @Around("execution(* com.example.demo..*(..))")
    public Object monitorEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        LOGGER.info("Method {} execution time: {} ms", joinPoint.getSignature(), executionTime);
        return result;
    }
}
