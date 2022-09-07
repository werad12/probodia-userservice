package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.record.MealDetail;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.MealDetailRepository;
import com.probodia.userservice.api.repository.record.MealRepository;
import com.probodia.userservice.api.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.probodia.userservice.converter.RecordConverter.mealConvert;

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
    public Meal saveMeal(User user, String timeTag, String recordDate) {

        Meal meal = new Meal();
        setRecordBase(meal,user,timeTag,recordDate);

        return meal;
    }

    @Transactional
    public MealResponseVO saveMeal(User user,MealVO request){

        Meal savedMeal = saveMeal(user, request.getTimeTag(), request.getRecordDate());
        MealResponseVO retValue = saveMealDetail(savedMeal, request.getMealDetails());

        return retValue;
    }

    public MealResponseVO saveMealDetail(Meal meal, List<MealDetailVO> mealDetails) {


        for(MealDetailVO requestDetail : mealDetails){
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

    public MealResponseVO updateMeal(Meal meal, MealUpdateVO requestRecord) {

        meal.setTimeTag(TimeTagCode.findByValue(requestRecord.getTimeTag()));
        meal.setRecordDate(requestRecord.getRecordDate());
        for(MealDetailUpdateVO detail : requestRecord.getMealDetails()){
            Optional<MealDetail> detailEntity =
                    mealDetailRepository.findById(detail.getMealDetailId());
            if(detailEntity.isEmpty())
                throw new NoSuchElementException("Not found Meal Detail matched by mealDetailId");

            MealDetail updateTarget = detailEntity.get();
            updateTarget.setQuantity(detail.getQuantity());
            updateTarget.setFoodName(detail.getFoodName());
            updateTarget.setCalorie(detail.getCalories());
            updateTarget.setImageUrl(detail.getImageUrl());
            updateTarget.setBloodSugar(detail.getBloodSugar());
            updateTarget.setFoodId(detail.getFoodId());

            mealDetailRepository.save(updateTarget);
        }

        Meal saved = mealRepository.save(meal);

        return mealConvert(saved);
    }

}
