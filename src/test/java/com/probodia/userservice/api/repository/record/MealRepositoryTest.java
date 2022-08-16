package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.BSugar;
import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.user.User;
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
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Slf4j
@ActiveProfiles("test")
class MealRepositoryTest {

    @Autowired
    MealRepository mealRepository;

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
    @DisplayName("음식 생성 테스트")
    void saveTest(){
        Meal meal = new Meal();

        User user = userRepository.findByUserId(userid);

        int totalCalories = 456;
        String recordDate = "2017-11-11 12:12:12";
        TimeTagCode timeTag = TimeTagCode.MORNING;
        String imageUrl = "http://asdf.com";


        meal.setUser(user);

        meal.setRecordDate(recordDate);
        meal.setTimeTag(timeTag);

        meal.setTotalCalories(totalCalories);
        meal.setTotalImageUrl(imageUrl);
        meal.setMealDetails(null);

        Meal saved = mealRepository.save(meal);

        assertThat(saved.getTotalCalories()).isEqualTo(totalCalories);
        assertThat(saved.getTotalImageUrl()).isEqualTo(imageUrl);

        assertThat(saved.getRecordDate()).isEqualTo(recordDate);
        assertThat(saved.getTimeTag()).isEqualTo(timeTag);
    }

}