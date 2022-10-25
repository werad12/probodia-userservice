package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.record.MealDetail;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.MealDetailRepository;
import com.probodia.userservice.api.repository.record.MealRepository;
import com.probodia.userservice.api.dto.meal.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Slf4j
public class MealService {

    private MealRepository mealRepository;
    private MealDetailRepository mealDetailRepository;

    @Autowired
    public MealService(MealRepository mealRepository, MealDetailRepository mealDetailRepository) {
        this.mealRepository = mealRepository;
        this.mealDetailRepository = mealDetailRepository;
    }

    private void setRecordBase(Records record, User user, String timeTag, String recordDate){
        record.setUser(user);
        record.setRecordDate(recordDate);
        record.setTimeTag(TimeTagCode.findByValue(timeTag));

    }

    //@Transactional
    private Meal saveMeal(User user, String timeTag, String recordDate) {

        Meal meal = new Meal();
        setRecordBase(meal,user,timeTag,recordDate);

        return meal;
    }

    @Transactional
    public MealResponseDto saveMeal(User user, MealDto request){

        Meal savedMeal = saveMeal(user, request.getTimeTag(), request.getRecordDate());
        MealResponseDto retValue = saveMealDetail(savedMeal, request.getMealDetails());

        return retValue;
    }

    private MealResponseDto saveMealDetail(Meal meal, List<MealDetailDto> mealDetails) {


        for(MealDetailDto requestDetail : mealDetails){
            MealDetail col = new MealDetail();

            col.setFoodName(requestDetail.getFoodName());
            col.setQuantity(requestDetail.getQuantity());

//            if(requestDetail.getFoodName().equals("오류"))
//                throw new IllegalArgumentException("오류 발생!");

            if(requestDetail.getCalories() != null){
                col.setCalorie(requestDetail.getCalories());
            }

            if(requestDetail.getImageUrl() != null){
                col.setImageUrl(requestDetail.getImageUrl());
            }

            if(requestDetail.getBloodSugar() != null){
                col.setBloodSugar(requestDetail.getBloodSugar());
            }

            if(requestDetail.getFoodId() != null){
                col.setFoodId(requestDetail.getFoodId());
            }

            meal.getMealDetails().add(col);
            mealDetailRepository.save(col);
        }

        Meal savedMeal = mealRepository.save(meal);


        return mealConvert(savedMeal);
    }



    public Optional<Meal> findMealByUserAndId(User user, Long recordId) {
        return mealRepository.findByUserAndId(user,recordId);
    }

    @Transactional
    public Long deleteMeal(Meal meal) {
        Long ret = meal.getId();
        /*
        List<MealDetail> mealDetails = meal.getMealDetails();
        for(MealDetail detail : mealDetails){
            mealDetailRepository.delete(detail);
        }
        */
        mealRepository.delete(meal);
        return ret;
    }

    private void deleteDetailByRecord(Meal meal){
        mealDetailRepository.deleteByMeal(meal);
    }

    @Transactional
    public MealResponseDto updateMeal(Meal meal, MealUpdateDto requestRecord) {

        meal.setTimeTag(TimeTagCode.findByValue(requestRecord.getTimeTag()));
        meal.setRecordDate(requestRecord.getRecordDate());

        deleteDetailByRecord(meal);

        Set<MealDetail> mealDetails = new HashSet<>();

        for(MealDetailDto m : requestRecord.getMealDetails()){
            MealDetail detail = new MealDetail();
            detail.setQuantity(m.getQuantity());
            detail.setFoodName(m.getFoodName());
            detail.setCalorie(m.getCalories());
            detail.setImageUrl(m.getImageUrl());
            detail.setBloodSugar(m.getBloodSugar());
            detail.setFoodId(m.getFoodId());

            mealDetailRepository.save(detail);
            mealDetails.add(detail);

        }

        meal.setMealDetails(mealDetails);

        Meal saved = mealRepository.save(meal);


        return mealConvert(saved);
    }

    private MealResponseDto mealConvert(Meal saved){
        List<MealDetailResponseDto> detailConverted = new ArrayList<>();
        for(MealDetail detail : saved.getMealDetails()){
            detailConverted.add(mealDetailConvert(detail));
        }

        return MealResponseDto.builder().recordId(saved.getId())
                .mealDetails(detailConverted)
                .timeTag(saved.getTimeTag().getValue())
                .recordDate(saved.getRecordDate()).userId(saved.getUser().getUserId())
                .build();

    }

    private MealDetailResponseDto mealDetailConvert(MealDetail saved){
        return MealDetailResponseDto.builder().mealDetailId(saved.getId())
                .foodName(saved.getFoodName()).imageUrl(saved.getImageUrl())
                .quantity(saved.getQuantity()).foodId(saved.getFoodId())
                .bloodSugar(saved.getBloodSugar()).calories(saved.getCalorie()).build();
    }

}
