package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.api.service.UserService;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DataJpaTest
class BPressureRepositoryTest {

    @Autowired
    BPressureRepository bPressureRepository;

    @Autowired
    UserRepository userRepository;

    String userid;

    @BeforeEach
    void before(){

        userid = "testtesttest";

        User user = new User(
                userid,
                "test1234",
                null,
                "Y",
                "test.com",
                ProviderType.KAKAO,
                RoleType.USER);


        userRepository.save(user);

    }



    @Test
    @DisplayName("혈압 생성 테스트")
    void saveTest(){
        BPressure bPressure = new BPressure();

        User user = userRepository.findByUserId(userid);

        bPressure.setUser(user);
        bPressure.setMaxBloodPressure(456);
        bPressure.setMinBloodPressure(123);



    }

}