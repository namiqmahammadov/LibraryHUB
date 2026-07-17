package com.namiq.msbook.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* com.namiq.msbook.service.*.*(..))")
    public void serviceLayer(){}
    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info(
                "[SERVICE] Calling {}.{} with args:{} ", className, methodName, Arrays.toString(args)
        );
    }

    @AfterReturning(pointcut = "serviceLayer()",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info(
                "[SERVICE] {}.{} returned: {}", className, methodName, result
        );

    }

    @AfterThrowing( pointcut = "serviceLayer()",
            throwing = "exception"      )
    public void logAfterThrowing(JoinPoint joinPoint,Exception exception){
        String className=joinPoint.getTarget().getClass().getSimpleName();
        String methodName=joinPoint.getSignature().getName();
        log.error(
                "[SERVICE] {}.{} threw {} : {}",
                className,
                methodName,
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                exception
        );
    }
}
