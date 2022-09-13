package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.record.MealDetail;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@ActiveProfiles("test")
class MealDetailRepositoryTest {
    @Autowired
    MealRepository mealRepository;

    @Autowired
    MealDetailRepository mealDetailRepository;

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
//    @DisplayName("음식 상세 정보 생성 테스트")
//    void saveTest(){
//        Meal meal = new Meal();
//
//        User user = userRepository.findByUserId(userid);
//
//        int totalCalories = 456;
//        String recordDate = "2017-11-11 12:12:12";
//        TimeTagCode timeTag = TimeTagCode.MORNING_AFTER;
//        String imageUrl = "http://asdf.com";
//
//        int quantity = 123;
//        int calory = 2323;
//        String foodId = "test-099";
//        int bSugar = 789;
//        String foodImageUrl = "http://errwr.com";
//        String foodName = "test";
//
//        meal.setUser(user);
//
//        meal.setRecordDate(recordDate);
//        meal.setTimeTag(timeTag);
//
//        meal.setTotalCalories(totalCalories);
//        meal.setTotalImageUrl(imageUrl);
//        meal.setMealDetails(null);
//
//        Meal saved = mealRepository.save(meal);
//
//        List<MealDetail> detailList = new ArrayList<>();
//
//        MealDetail mealDetail = new MealDetail();
//        mealDetail.setMeal(saved);
//        mealDetail.setQuantity(quantity);
//        mealDetail.setCalorie(calory);
//        mealDetail.setFoodId(foodId);
//        mealDetail.setBloodSugar(bSugar);
//        mealDetail.setImageUrl(foodImageUrl);
//        mealDetail.setFoodName(foodName);
//
//        MealDetail savedDetail = mealDetailRepository.save(mealDetail);
//
//        detailList.add(savedDetail);
//        meal.setMealDetails(detailList);
//
//        saved = mealRepository.save(saved);
//
//        assertThat(saved.getTotalCalories()).isEqualTo(totalCalories);
//        assertThat(saved.getTotalImageUrl()).isEqualTo(imageUrl);
//
//        assertThat(saved.getRecordDate()).isEqualTo(recordDate);
//        assertThat(saved.getTimeTag()).isEqualTo(timeTag);
//
//        MealDetail comp = saved.getMealDetails().get(0);
//
//        assertThat(comp.getMeal()).isEqualTo(saved);
//
//        assertThat(comp.getBloodSugar()).isEqualTo(bSugar);
//        assertThat(comp.getCalorie()).isEqualTo(calory);
//        assertThat(comp.getFoodId()).isEqualTo(foodId);
//        assertThat(comp.getFoodName()).isEqualTo(foodName);
//        assertThat(comp.getImageUrl()).isEqualTo(foodImageUrl);
//        assertThat(comp.getQuantity()).isEqualTo(quantity);
//
//    }


}