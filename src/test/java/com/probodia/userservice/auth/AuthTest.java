package com.probodia.userservice.auth;


import com.probodia.userservice.oauth.token.AuthToken;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@Slf4j
@SpringBootTest
public class AuthTest {

//    @Autowired
//    AuthTokenProvider tokenProvider;
//
//    @Test
//    void expiredDateTest() {
//
//        String token  = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMzQzMzQxMTAxIiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTAzMDA0NjEwNDl9.ZucTw5D1RSfLqbPkG_PfiyOkppO6SH1B6BFJNjsHzuA";
//
//        AuthToken authToken = tokenProvider.convertAuthToken(token);
//
//        Date expiration = authToken.getTokenClaims().getExpiration();
//        log.info("date : {}",expiration);
//
//    }
//
//    @Test
//    void expiredDateTest2() {
//
//        String token  = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCUSIsInJvbGUiOiJST0xFX1VTRVIiLCJleHAiOjEwMzAwNDYxMDQ5fQ.qDyoYM6xHiGu1aG9qX0tahC1k2Bh3NPnvz5D-oHrPH4";
//        AuthToken authToken = tokenProvider.convertAuthToken(token);
//
//        Date expiration = authToken.getTokenClaims().getExpiration();
//        log.info("date : {}",expiration);
//
//    }


}
