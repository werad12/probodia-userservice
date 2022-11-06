package com.probodia.userservice.api.repository.user;

import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@DataJpaTest
@Slf4j
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("유저 생성 테스트")
    void createTest(){
        User user = new User(
                "testtesttest",
                "test1234",
                null,
                "Y",
                "test.com",
                ProviderType.KAKAO,
                RoleType.USER);


        userRepository.save(user);

        User savedUser = userRepository.findByUserId("testtesttest");

        assertThat(savedUser.getUserId()).isEqualTo("testtesttest");
        log.debug("user seq : {}",user.getUserSeq());
        assertThat(savedUser.getUsername()).isEqualTo("test1234");
        assertThat(savedUser.getProfileImageUrl()).isEqualTo("test.com");

    }

    @Test
    @DisplayName("유저 생성 테스트2")
    void createTest2(){
        User user = new User(
                "testtesttest",
                "test1234",
                null,
                "Y",
                "test.com",
                ProviderType.KAKAO,
                RoleType.USER);


        userRepository.save(user);

        User savedUser = userRepository.findByUserId("testtesttest");

        assertThat(savedUser.getUserId()).isEqualTo("testtesttest");
        log.debug("user seq2 : {}",user.getUserSeq());
        assertThat(savedUser.getUsername()).isEqualTo("test1234");
        assertThat(savedUser.getProfileImageUrl()).isEqualTo("test.com");

    }



}