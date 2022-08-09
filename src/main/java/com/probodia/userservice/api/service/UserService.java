package com.probodia.userservice.api.service;

import com.probodia.userservice.api.entity.auth.AuthReqModel;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.api.vo.UserInfoRequestVO;
import com.probodia.userservice.api.vo.UserInfoVO;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import com.probodia.userservice.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final Environment env;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public Map<String,String> authenticate(AuthReqModel authReqModel) {
        String oauthAccessToken = authReqModel.getOauthAccessToken();
        String url = env.getProperty("tokenVerifyUri");

        Map<String,String> ret = new HashMap<>();


        ResponseEntity<Map> response = getHttpResponseFromAuthServer(oauthAccessToken,url);
        String statusCode = String.valueOf(response.getStatusCodeValue());

        ret.put("status",statusCode);
        log.info("Kakao return status : {}",statusCode);
        if(statusCode.equals("200")){
            ret.put("id",String.valueOf((Long)response.getBody().get("id")));
        }

        return ret;
    }

    private ResponseEntity<Map> getHttpResponseFromAuthServer(String oauthAccessToken,String url){
        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(oauthAccessToken);
        HttpEntity req = new HttpEntity(header);
        return restTemplate.exchange(url, HttpMethod.GET, req, Map.class);
    }

    public Map<String, String> getUserInfo(AuthReqModel authReqModel) {
        String oauthAccessToken = authReqModel.getOauthAccessToken();
        String url = env.getProperty("spring.security.oauth2.client.provider.kakao.userInfoUri");
        ResponseEntity<Map> response = getHttpResponseFromAuthServer(oauthAccessToken, url);
        String id = String.valueOf((Long)response.getBody().get("id"));
        Map<String,String> properties = (Map<String, String>) response.getBody().get("properties");
        String name = properties.get("nickname");
        String imageUrl = properties.get("thumbnail_image");

        log.info("[UserInfo] : username = {}",name);

        Map<String,String> ret = new HashMap<>();
        ret.put("id",id);
        ret.put("name",name);
        ret.put("imageUrl",imageUrl);

        return ret;
    }

    public User createUser(Map<String, String> userInfo) {

        log.info("[User] : username = {}",userInfo.get("name"));

        User user = new User(
                userInfo.get("id"),
                userInfo.get("name"),
                null,
                "Y",
                userInfo.get("imageUrl"),
                ProviderType.KAKAO,
                RoleType.USER);

        return userRepository.saveAndFlush(user);
    }

    public UserInfoVO getUserInfo(User user){
        return UserInfoVO.builder().userId(user.getUserId())
                .height(user.getHeight()).sex(user.getSex())
                .weight(user.getWeight()).profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    public UserInfoVO updateUserInfo(UserInfoRequestVO request, User user) {

        if(request.getHeight()!=null){
            user.setHeight(request.getHeight());
        }

        if(request.getSex()!=null){
            user.setSex(request.getSex());
        }

        if(request.getWeight()!=null){
            user.setWeight(request.getWeight());
        }

        if(request.getProfileImageUrl()!=null){
            user.setProfileImageUrl(request.getProfileImageUrl());
        }

        userRepository.save(user);

        return UserInfoVO.builder().userId(user.getUserId())
                .height(user.getHeight()).sex(user.getSex())
                .weight(user.getWeight()).profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
