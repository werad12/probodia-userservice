package com.probodia.userservice.kakao;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
@Slf4j
public class KakaoLoginTest {

    private String token = "RfXbqV9lBXDaygO2DIGIsCftujbzXrHHWVjTGVrsCj1zmwAAAYJM8ard";
    RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());


    private final Environment environment;
    @Autowired
    public KakaoLoginTest(Environment environment) {
        this.environment = environment;
    }

    static class TestResponse{
        String id;
        Integer expires_in;
        Integer app_id;
    }

    @Test
    void getTokenInfo(){
        String url = "https://kapi.kakao.com/v1/user/access_token_info";

        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(token);

        HttpEntity req = new HttpEntity(header);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, req, Map.class);

        log.error("test Response : {}",response.getStatusCodeValue());
        log.error("test Id : {}",response.getBody().get("id"));

    }

    private ResponseEntity<Map> getHttpResponseFromAuthServer(String oauthAccessToken,String url){
        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(oauthAccessToken);
        HttpEntity req = new HttpEntity(header);
        return restTemplate.exchange(url, HttpMethod.GET, req, Map.class);
    }


    @Test
    void getUserInfo(){

        String oauthAccessToken = token;
        String url = "https://kapi.kakao.com/v2/user/me";
        ResponseEntity<Map> response = getHttpResponseFromAuthServer(oauthAccessToken, url);
        String id = String.valueOf((Long)response.getBody().get("id"));
        Map<String,String> properties = (Map<String, String>) response.getBody().get("properties");
        String name = properties.get("nickname");
        String imageUrl = properties.get("thumbnail_image");
        log.debug("id : {}",id);
        log.debug("name : {}", name);
        log.debug("image Url : {}",imageUrl);

    }

    @Test
    void getEnv(){
        String tokenVerifyUri = environment.getProperty("spring.security.oauth2.client.provider.kakao.userInfoUri");
        log.debug(tokenVerifyUri);
    }

}
