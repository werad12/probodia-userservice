package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.BSugar;
import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import com.probodia.userservice.testutil.BeforeEachMethod;
import com.probodia.userservice.testutil.TestStaticVariable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@ActiveProfiles("test")
class MedicineRepositoryTest {


    @Autowired
    MedicineRepository medicineRepository;

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
//    @Test
//    @DisplayName("투약 생성 테스트")
//    void saveTest(){
//        Medicine medicine = new Medicine();
//
//        User user = userRepository.findByUserId(userid);
//
//        String medicineName = "test Medicine";
//        String medicineId = "TEST-001";
//        int quantity = 456;
//        String recordDate = "2017-11-11 12:12:12";
//        TimeTagCode timeTag = TimeTagCode.MORNING_AFTER;
//
//
//        medicine.setUser(user);
//
//        medicine.setRecordDate(recordDate);
//        medicine.setTimeTag(timeTag);
//
//        medicine.setMedicineId(medicineId);
//        medicine.setMedicineName(medicineName);
//        medicine.setMedicineCnt(quantity);
//
//        Medicine saved = medicineRepository.save(medicine);
//
//        assertThat(saved.getMedicineCnt()).isEqualTo(quantity);
//        assertThat(saved.getMedicineId()).isEqualTo(medicineId);
//        assertThat(saved.getMedicineName()).isEqualTo(medicineName);
//
//        assertThat(saved.getRecordDate()).isEqualTo(recordDate);
//        assertThat(saved.getTimeTag()).isEqualTo(timeTag);
//
//    }

}