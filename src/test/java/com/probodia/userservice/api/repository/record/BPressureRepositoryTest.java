package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Slf4j
@ActiveProfiles("test")
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

        int maxBP = 456;
        int minBP = 123;
        int heartB = 1234;
        String recordDate = "2017-11-11 12:12:12";
        TimeTagCode timeTag = TimeTagCode.MORNING;


        bPressure.setUser(user);

        bPressure.setRecordDate(recordDate);
        bPressure.setTimeTag(timeTag);

        bPressure.setMaxBloodPressure(maxBP);
        bPressure.setMinBloodPressure(minBP);
        bPressure.setHeartBeat(heartB);

        BPressure saved = bPressureRepository.save(bPressure);

        assertThat(saved.getMaxBloodPressure()).isEqualTo(maxBP);
        assertThat(saved.getMinBloodPressure()).isEqualTo(minBP);
        assertThat(saved.getHeartBeat()).isEqualTo(heartB);

        assertThat(saved.getRecordDate()).isEqualTo(recordDate);
        assertThat(saved.getTimeTag()).isEqualTo(timeTag);
    }




}