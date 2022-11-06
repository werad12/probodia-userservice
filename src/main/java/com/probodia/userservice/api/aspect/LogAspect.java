package com.probodia.userservice.api.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.probodia.userservice.api.controller..*.*(..))")
    private void controllerCut() {}

    @Before("controllerCut()")
    public void beforeControllerLog(JoinPoint joinPoint){

        Method method = getMethod(joinPoint);
        log.info("[Controller] - [{}] Request", method.getName());
    }

    @AfterReturning(value = "controllerCut()", returning = "returnObj")
    public void controllerAfterReturnLog(JoinPoint joinPoint, Object returnObj){

        Method method = getMethod(joinPoint);
        log.info("[Controller] - [{}] Response", method.getName());

    }


    @Pointcut("execution(* com.probodia.userservice.api.service..*.*(..))")
    private void serviceCut() {}

    @Before("serviceCut()")
    public void beforeServiceLog(JoinPoint joinPoint){

        Method method = getMethod(joinPoint);
        log.info("[Service] - [{}] Request", method.getName());
    }

    @AfterReturning(value = "serviceCut()", returning = "returnObj")
    public void serviceAfterReturnLog(JoinPoint joinPoint, Object returnObj){

        Method method = getMethod(joinPoint);
        log.info("[Service] - [{}] Response", method.getName());

    }
    @Around("@annotation(com.probodia.userservice.api.annotation.BasicError)")
    public Object basicErrorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        final String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        final Object[] arguments = joinPoint.getArgs();
        Exception e = (Exception) arguments[0];

        log.warn("[Error] - [{}] : {}", e.getClass(), e.getMessage());

        return joinPoint.proceed();
    }

    @Around("@annotation(com.probodia.userservice.api.annotation.ValidationError)")
    public Object validationErrorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        final String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        final Object[] arguments = joinPoint.getArgs();

        BindingResult bindingResult = (BindingResult) arguments[0];

        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(c -> errors.put(((FieldError)c).getField() , c.getDefaultMessage()));

        log.warn("[Validation Error] - [{}]", errors);

        return joinPoint.proceed();
    }


    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

}
