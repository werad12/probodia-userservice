package com.probodia.userservice.api.entity.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.BSugarRepository;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
@ActiveProfiles("test")
class BSugarTest {

    @Autowired
    BSugarRepository bSugarRepository;

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
    @DisplayName("혈당 생성 테스트")
    void saveTest(){
        BSugar bSugar = new BSugar();

        User user = userRepository.findByUserId(userid);

        int bloodSugar = 456;
        String recordDate = "2017-11-11 12:12:12";
        TimeTagCode timeTag = TimeTagCode.MORNING_AFTER;


        bSugar.setUser(user);

        bSugar.setRecordDate(recordDate);
        bSugar.setTimeTag(timeTag);

        bSugar.setBloodSugar(bloodSugar);

        BSugar saved = bSugarRepository.save(bSugar);

        assertThat(saved.getBloodSugar()).isEqualTo(bloodSugar);

        assertThat(saved.getRecordDate()).isEqualTo(recordDate);
        assertThat(saved.getTimeTag()).isEqualTo(timeTag);
    }


}