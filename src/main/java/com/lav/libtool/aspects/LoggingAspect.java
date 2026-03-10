package com.lav.libtool.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@Profile({"dev","int"})
public class LoggingAspect {

    @Pointcut("execution(* com.lav.libtool.service..*(..))")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.debug("Entering {} with args {}", method, Arrays.toString(args));

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - start;

            log.debug("Exiting {} with result {} ({} ms)",
                    method,
                    result,
                    executionTime);

            return result;

        } catch (Exception ex) {

            long executionTime = System.currentTimeMillis() - start;

            log.error("Exception in {} after {} ms",
                    method,
                    executionTime,
                    ex);

            throw ex;
        }
    }

}