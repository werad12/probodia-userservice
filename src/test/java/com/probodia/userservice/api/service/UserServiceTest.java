package com.probodia.userservice.api.service;

import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserService userService;


//    @Autowired
//    UserRepository userRepository;

//    private User saveUser(){
//        User user = new User(
//                "testtesttest",
//                "test1234",
//                null,
//                "Y",
//                "test.com",
//                ProviderType.KAKAO,
//                RoleType.USER);
//
//
//        return userRepository.save(user);
//    }

    @Test
    @DisplayName("createUser 메서드 테스트")
    void createUser() {

        String username = "testuser";
        String userId = "testId";
        String imageUrl  ="test.com";

        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("name",username);
        userInfo.put("id",userId);
        userInfo.put("imageUrl",imageUrl);

        User createUser = userService.createUser(userInfo);
        //User byUserId = userRepository.findByUserId(userId);

        log.debug("User : {}",createUser);

        //Assertions.assertThat(createUser).isEqualTo(byUserId);
    }

    @Test
    void testGetUserInfo() {
    }

    @Test
    void updateUserInfo() {
    }
}