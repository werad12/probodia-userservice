package com.probodia.userservice.api.service;

import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private User saveUser(){
        User user = new User(
                "testtesttest",
                "test1234",
                null,
                "Y",
                "test.com",
                ProviderType.KAKAO,
                RoleType.USER);


        return userRepository.save(user);
    }

    @Test
    void getUser() {


    }

    @Test
    void authenticate() {
    }

    @Test
    void getUserInfo() {
    }

    @Test
    void createUser() {
    }

    @Test
    void testGetUserInfo() {
    }

    @Test
    void updateUserInfo() {
    }
}