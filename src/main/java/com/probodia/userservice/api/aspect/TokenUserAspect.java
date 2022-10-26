package com.probodia.userservice.api.aspect;

import com.probodia.userservice.api.annotation.TokenRequired;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenUserAspect {

    private final AuthTokenProvider tokenProvider;
    private final UserService userService;

    @Around("execution(* *(.., @com.probodia.userservice.api.annotation.TokenRequired (*), ..))")
    public Object getUserByToken(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request =requestAttributes.getRequest();

        String token = request.getHeader("Authorization");
        String userId = tokenProvider.getTokenSubject(token.substring(7));
        User user = userService.getUser(userId);

        Object[] args = Arrays.stream(joinPoint.getArgs()).map(data -> {
            if(data instanceof User) {
                data = user;
            }
            return data; }).toArray();

        return joinPoint.proceed(args);

    }

}
