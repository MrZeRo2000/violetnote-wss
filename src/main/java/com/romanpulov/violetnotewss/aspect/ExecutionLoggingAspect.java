package com.romanpulov.violetnotewss.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ExecutionLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.romanpulov.violetnotewss.controller.*.*(..))")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.toLongString();

        logger.debug("Before " + methodName);
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        logger.debug("After " + methodName);

        long elapsedTime = endTime - startTime;
        long elapsedTimeInMS = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.MILLISECONDS);
        logger.info(String.format("Executed %s in %d ms", methodName, elapsedTimeInMS));

        return result;
    }
}
