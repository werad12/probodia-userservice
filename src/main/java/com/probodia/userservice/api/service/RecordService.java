package com.probodia.userservice.api.service;

import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.record.BSugar;
import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.record.MealDetail;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.BPressureRepository;
import com.probodia.userservice.api.repository.record.BSugarRepository;
import com.probodia.userservice.api.repository.record.MealDetailRepository;
import com.probodia.userservice.api.repository.record.MealRepository;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.api.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    private BPressureRepository bPressureRepository;
    private BSugarRepository bSugarRepository;
    private MealRepository mealRepository;
    private MealDetailRepository mealDetailRepository;

    @Autowired
    public RecordService(BPressureRepository bPressureRepository, BSugarRepository bSugarRepository,
                         MealRepository mealRepository, MealDetailRepository mealDetailRepository) {
        this.bPressureRepository = bPressureRepository;
        this.bSugarRepository = bSugarRepository;
        this.mealRepository = mealRepository;
        this.mealDetailRepository = mealDetailRepository;
    }

    public BSugarResponse saveSugar(String timeTag, Integer bloodSugar, User user) {

        BSugar bSugar = new BSugar();
        bSugar.setUser(user);
        bSugar.setTimeTag(timeTag);
        bSugar.setBloodSugar(bloodSugar);
        BSugar saved = bSugarRepository.save(bSugar);

        return
                BSugarResponse.builder().bloodSugar(bloodSugar)
                        .timeTag(timeTag)
                        .userId(user.getUserId())
                        .recordId(saved.getId())
                        .build();
    }

    public BPressureResponse savePressure(String timeTag, Integer bloodPressure, User user) {

        BPressure bPressure = new BPressure();
        bPressure.setUser(user);
        bPressure.setTimeTag(timeTag);
        bPressure.setBloodPressure(bloodPressure);
        BPressure saved = bPressureRepository.save(bPressure);

        return
                BPressureResponse.builder().bloodPressure(bloodPressure)
                        .timeTag(timeTag)
                        .userId(user.getUserId())
                        .recordId(saved.getId())
                        .build();
    }

    public BSugarResponse updateBSugar(BSugar sugar,BSugarUpdateVO updateVO){
        sugar.setBloodSugar(updateVO.getBloodSugar());
        sugar.setTimeTag(updateVO.getTimeTag());

        bSugarRepository.save(sugar);

        return
                BSugarResponse.builder().bloodSugar(updateVO.getBloodSugar())
                        .timeTag(updateVO.getTimeTag())
                        .userId(sugar.getUser().getUserId())
                        .recordId(sugar.getId())
                        .build();
    }

    public BPressureResponse updateBPressure(BPressure bPressure, BPressureUpdateVO requestRecord) {
        bPressure.setBloodPressure(requestRecord.getBloodPressure());
        bPressure.setTimeTag(requestRecord.getTimeTag());

        bPressureRepository.save(bPressure);

        return
                BPressureResponse.builder().bloodPressure(bPressure.getBloodPressure())
                        .timeTag(bPressure.getTimeTag())
                        .userId(requestRecord.getUserId())
                        .recordId(bPressure.getId())
                        .build();

    }

    public Optional<BSugar> findBSugarByUserAndId(User user, Long recordId) {
        return bSugarRepository.findByUserAndId(user,recordId);
    }

    public Long deleteBSugar(BSugar deleteRecord) {
        bSugarRepository.delete(deleteRecord);
        return deleteRecord.getId();
    }

    public Optional<BPressure> findBPressureByUserAndId(User user, Long recordId) {
        return bPressureRepository.findByUserAndId(user,recordId);
    }

    public Long deleteBPressure(BPressure deleteRecord) {
        bPressureRepository.delete(deleteRecord);
        return deleteRecord.getId();
    }

    public Meal saveMeal(User user, String timeTag) {

        Meal meal = new Meal();
        meal.setTimeTag(timeTag);
        meal.setUser(user);

        return meal;

    }

    public MealResponseVO saveMealDetail(Meal meal, List<MealDetailVO> mealDetails) {

        MealResponseVO result = new MealResponseVO();

        result.setTimeTag(meal.getTimeTag());
        result.setUserId(meal.getUser().getUserId());

        List<MealDetailResponseVO> resultDetails = new ArrayList<>();

        for(MealDetailVO requestDetail : mealDetails){
            MealDetail col = new MealDetail();
            MealDetailResponseVO resultCol = new MealDetailResponseVO();

            col.setFoodName(requestDetail.getFoodName());
            resultCol.setFoodName(requestDetail.getFoodName());


            if(requestDetail.getCalories() != null){
                col.setCalorie(requestDetail.getCalories());
                resultCol.setCalories(requestDetail.getCalories());
            }

            if(requestDetail.getImageUrl() != null){
                col.setImageUrl(requestDetail.getImageUrl());
                resultCol.setImageUrl(requestDetail.getImageUrl());
            }

            if(requestDetail.getBloodSugar() != null){
                col.setBloodSugar(requestDetail.getBloodSugar());
                resultCol.setBloodSugar(requestDetail.getBloodSugar());
            }

            if(requestDetail.getFoodId() != null){
                col.setFoodId(requestDetail.getFoodId());
            }

            meal.getMealDetails().add(col);

            MealDetail saved = mealDetailRepository.save(col);
            resultCol.setMealDetailId(saved.getId());
            resultDetails.add(resultCol);
        }

        result.setMealDetails(resultDetails);
        Meal savedMeal = mealRepository.save(meal);
        result.setMealId(savedMeal.getId());


        return result;
    }
}
