package com.probodia.userservice.api.service.user;

import com.probodia.userservice.api.entity.auth.AuthReqModel;
import com.probodia.userservice.api.entity.enums.base.DiabeteCode;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.RecordRepository;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.api.dto.user.UserInfoRequestDto;
import com.probodia.userservice.api.dto.user.UserInfoDto;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final RecordRepository recordRepository;

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
        log.debug("Kakao return status : {}",statusCode);
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

        log.debug("[UserInfo] : username = {}",name);

        Map<String,String> ret = new HashMap<>();
        ret.put("id",id);
        ret.put("name",name);
        ret.put("imageUrl",imageUrl);

        return ret;
    }

    public User createUser(Map<String, String> userInfo) {

        log.debug("[User] : username = {}",userInfo.get("name"));

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

    public UserInfoDto getUserInfo(User user){
        return UserInfoDto.builder().userId(user.getUserId())
                .height(user.getHeight()).sex(user.getSex())
                .weight(user.getWeight()).profileImageUrl(user.getProfileImageUrl())
                .diabeteCode(user.getDiabeteCode()==null? null : user.getDiabeteCode().getValue()).age(user.getAge())
                .username(user.getUsername()).point(user.getPoint())
                .build();
    }

    @Transactional
    public UserInfoDto updateUserInfo(UserInfoRequestDto request, User user) {

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

        if(request.getAge()!=null){
            log.debug("age : {}",request.getAge());
            user.setAge(request.getAge());
        }

        if(request.getDiabeteCode()!=null){

            user.setDiabeteCode(DiabeteCode.findByValue(request.getDiabeteCode()));
        }

        userRepository.save(user);

        return UserInfoDto.builder().userId(user.getUserId())
                .height(user.getHeight()).sex(user.getSex())
                .weight(user.getWeight()).profileImageUrl(user.getProfileImageUrl())
                .diabeteCode(user.getDiabeteCode().getValue()).age(user.getAge())
                .username(user.getUsername())
                .build();
    }

    @Transactional
    public String deleteUser(User user) {
        String userId = user.getUserId();
        recordRepository.deleteAllByUser(user);
        userRepository.delete(user);
        return userId;
    }

    @Transactional
    public void updatePoint(String userId, Integer point) {
        User byUserId = userRepository.findByUserId(userId);
        if(byUserId==null) throw new IllegalArgumentException("Not found user");
        Integer prev = byUserId.getPoint();
        byUserId.setPoint(prev + point);
    }
}
