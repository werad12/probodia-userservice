package com.probodia.userservice.api.service;

import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.*;
import com.probodia.userservice.api.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RecordService {

    private BPressureRepository bPressureRepository;
    private BSugarRepository bSugarRepository;
    private MealRepository mealRepository;
    private MealDetailRepository mealDetailRepository;
    private RecordRepository recordRepository;
    private MedicineRepository medicineRepository;

    @Autowired
    public RecordService(BPressureRepository bPressureRepository, BSugarRepository bSugarRepository, MealRepository mealRepository,
                         MealDetailRepository mealDetailRepository, RecordRepository recordRepository, MedicineRepository medicineRepository) {
        this.bPressureRepository = bPressureRepository;
        this.bSugarRepository = bSugarRepository;
        this.mealRepository = mealRepository;
        this.mealDetailRepository = mealDetailRepository;
        this.recordRepository = recordRepository;
        this.medicineRepository = medicineRepository;
    }

    public BSugarResponse saveSugar(String timeTag, Integer bloodSugar, User user) {

        BSugar bSugar = new BSugar();
        bSugar.setUser(user);
        bSugar.setTimeTag(timeTag);
        bSugar.setBloodSugar(bloodSugar);
        BSugar saved = bSugarRepository.save(bSugar);

        return bSugarConvert(saved);
    }

    public BSugarResponse bSugarConvert(BSugar bSugar){
        return
                BSugarResponse.builder().bloodSugar(bSugar.getBloodSugar())
                        .timeTag(bSugar.getTimeTag())
                        .userId(bSugar.getUser().getUserId())
                        .recordId(bSugar.getId())
                        .build();
    }

    public BPressureResponse savePressure(String timeTag, Integer bloodPressure, User user) {

        BPressure bPressure = new BPressure();
        bPressure.setUser(user);
        bPressure.setTimeTag(timeTag);
        bPressure.setBloodPressure(bloodPressure);
        BPressure saved = bPressureRepository.save(bPressure);

        return bPressureConvert(saved);
    }

    public BPressureResponse bPressureConvert(BPressure bPressure){
        return
                BPressureResponse.builder().bloodPressure(bPressure.getBloodPressure())
                        .timeTag(bPressure.getTimeTag())
                        .userId(bPressure.getUser().getUserId())
                        .recordId(bPressure.getId())
                        .build();
    }




    public BSugarResponse updateBSugar(BSugar sugar,BSugarUpdateVO updateVO){
        sugar.setBloodSugar(updateVO.getBloodSugar());
        sugar.setTimeTag(updateVO.getTimeTag());

        BSugar saved = bSugarRepository.save(sugar);

        return bSugarConvert(saved);
    }

    public BPressureResponse updateBPressure(BPressure bPressure, BPressureUpdateVO requestRecord) {
        bPressure.setBloodPressure(requestRecord.getBloodPressure());
        bPressure.setTimeTag(requestRecord.getTimeTag());

        BPressure saved = bPressureRepository.save(bPressure);

        return
                bPressureConvert(saved);

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



        List<MealDetailResponseVO> resultDetails = new ArrayList<>();

        for(MealDetailVO requestDetail : mealDetails){
            MealDetail col = new MealDetail();

            col.setFoodName(requestDetail.getFoodName());


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

    public MealResponseVO mealConvert(Meal saved){
        List<MealDetailResponseVO> detailConverted = new ArrayList<>();
        for(MealDetail detail : saved.getMealDetails()){
            detailConverted.add(mealDetailConvert(detail));
        }

        return MealResponseVO.builder().mealId(saved.getId())
                .mealDetails(detailConverted)
                .timeTag(saved.getTimeTag()).userId(saved.getUser().getUserId())
                .build();

    }

    MealDetailResponseVO mealDetailConvert(MealDetail saved){
        return MealDetailResponseVO.builder().mealDetailId(saved.getId())
                .foodName(saved.getFoodName()).imageUrl(saved.getImageUrl())
                .bloodSugar(saved.getBloodSugar()).calories(saved.getCalorie()).build();
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

        meal.setTimeTag(requestRecord.getTimeTag());
        for(MealDetailUpdateVO detail : requestRecord.getMealDetails()){
            Optional<MealDetail> detailEntity =
                    mealDetailRepository.findById(detail.getMealDetailId());
            if(detailEntity.isEmpty())
                throw new NoSuchElementException("Not found Meal Detail matched by mealDetailId");

            MealDetail updateTarget = detailEntity.get();
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




    public List<Records> findAllByUser(User user) {
        return recordRepository.findAllByUser(user);
    }

    public MedicineResponseVO saveMedicine(MedicineVO requestRecord, User user) {

        Medicine medicine = new Medicine();
        medicine.setUser(user);
        medicine.setTimeTag(requestRecord.getTimeTag());
        if(requestRecord.getMedicineId()!=null)
            medicine.setMedicineId(requestRecord.getMedicineId());

        medicine.setMedicineCnt(requestRecord.getMedicineCnt());
        medicine.setMedicineName(requestRecord.getMedicineName());
        Medicine saved = medicineRepository.save(medicine);


        return convertMedicine(saved);
    }

    public MedicineResponseVO convertMedicine(Medicine saved){
        return MedicineResponseVO.builder().medicineId(saved.getMedicineId())
                .recordId(saved.getId()).medicineCnt(saved.getMedicineCnt())
                .medicineName(saved.getMedicineName()).timeTag(saved.getTimeTag())
                .userId(saved.getUser().getUserId()).build();
    }

    public Optional<Medicine> findMedicineByUserAndId(User user, Long recordId) {
        return medicineRepository.findByUserAndId(user,recordId);
    }

    public Long deleteMedicine(Medicine medicine) {
        medicineRepository.delete(medicine);
        return medicine.getId();
    }

    public MedicineResponseVO updateMedicine(Medicine medicine, MedicineUpdateVO requestRecord) {
        if(requestRecord.getMedicineId()!=null)
            medicine.setMedicineId(requestRecord.getMedicineId());
        medicine.setMedicineCnt(requestRecord.getMedicineCnt());
        medicine.setMedicineName(requestRecord.getMedicineName());

        Medicine saved = medicineRepository.save(medicine);

        return convertMedicine(saved);
    }
}
