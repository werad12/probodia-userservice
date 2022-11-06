package com.probodia.userservice.auth;


import com.probodia.userservice.oauth.token.AuthToken;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@Slf4j
@SpringBootTest
public class AuthTest {

    @Autowired
    AuthTokenProvider provider;

    @Test
    void testAuth(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMzc1NzIyOTM4Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY2MjgxOTE0Nn0.ZWwld92f78cTMlx2Dcwcat00KYYi2TXz8yuGsQa8Da0";
        String token2 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMzQzMzQxMTAxIiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTAzMDA0NjEwNDl9.ZucTw5D1RSfLqbPkG_PfiyOkppO6SH1B6BFJNjsHzuA";

        AuthToken authToken = provider.convertAuthToken(token);

        authToken.validate();

        log.debug("--------------");

        Claims expiredTokenClaims = authToken.getExpiredTokenClaims();

    }

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
//        log.debug("date : {}",expiration);
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
//        log.debug("date : {}",expiration);
//
//    }


}
