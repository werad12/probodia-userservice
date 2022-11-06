package com.probodia.userservice.interceptor;

import com.probodia.userservice.oauth.token.AuthToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER="Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            log.debug("AUTH : {}, {}, {}",authentication.getAuthorities(),authentication.getCredentials(),authentication.getDetails());
            AuthToken token = (AuthToken) authentication.getCredentials();
            log.debug("TOKEN : {}",token.getToken());
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, token.getToken()));
        }
    }
}