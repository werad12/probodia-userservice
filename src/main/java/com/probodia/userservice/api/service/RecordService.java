package com.probodia.userservice.api.service;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.*;
import com.probodia.userservice.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
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

    private void setRecordBase(Records record, User user, String timeTag, String recordDate){
        record.setUser(user);
        record.setRecordDate(recordDate);
        record.setTimeTag(TimeTagCode.findByValue(timeTag));

    }

    public BSugarResponse saveSugar(BSugarVO request, User user) {

        BSugar bSugar = new BSugar();
        setRecordBase(bSugar,user,request.getTimeTag(),request.getRecordDate());
        bSugar.setBloodSugar(request.getBloodSugar());

        BSugar saved = bSugarRepository.save(bSugar);

        return bSugarConvert(saved);
    }

    public BSugarResponse bSugarConvert(BSugar bSugar){
        return
                BSugarResponse.builder().bloodSugar(bSugar.getBloodSugar())
                        .timeTag(bSugar.getTimeTag().getValue())
                        .recordId(bSugar.getId())
                        .recordDate(bSugar.getRecordDate())
                        .build();
    }

    public BPressureResponse savePressure(BPressureVO request, User user) {

        BPressure bPressure = new BPressure();
        setRecordBase(bPressure,user,request.getTimeTag(),request.getRecordDate());
        bPressure.setMaxBloodPressure(request.getMaxBloodPressure());
        bPressure.setMinBloodPressure(request.getMinBloodPressure());
        bPressure.setHeartBeat(request.getHeartBeat());
        BPressure saved = bPressureRepository.save(bPressure);

        return bPressureConvert(saved);
    }

    public BPressureResponse bPressureConvert(BPressure bPressure){
        return
                BPressureResponse.builder()
                        .heartBeat(bPressure.getHeartBeat())
                        .maxBloodPressure(bPressure.getMaxBloodPressure())
                        .minBloodPressure(bPressure.getMinBloodPressure())
                        .timeTag(bPressure.getTimeTag().getValue())
                        .recordDate(bPressure.getRecordDate())
                        .recordId(bPressure.getId())
                        .build();
    }




    public BSugarResponse updateBSugar(BSugar sugar,BSugarUpdateVO updateVO){
        sugar.setBloodSugar(updateVO.getBloodSugar());
        sugar.setTimeTag(TimeTagCode.findByValue(updateVO.getTimeTag()));
        sugar.setRecordDate(updateVO.getRecordDate());

        BSugar saved = bSugarRepository.save(sugar);

        return bSugarConvert(saved);
    }

    public BPressureResponse updateBPressure(BPressure bPressure, BPressureUpdateVO requestRecord) {
        bPressure.setMinBloodPressure(requestRecord.getMinBloodPressure());
        bPressure.setMaxBloodPressure(requestRecord.getMaxBloodPressure());
        bPressure.setHeartBeat(requestRecord.getHeartBeat());
        bPressure.setTimeTag(TimeTagCode.findByValue(requestRecord.getTimeTag()));
        bPressure.setRecordDate(requestRecord.getRecordDate());

        BPressure saved = bPressureRepository.save(bPressure);

        return bPressureConvert(saved);
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

    public Meal saveMeal(User user, String timeTag, String recordDate) {

        Meal meal = new Meal();
        setRecordBase(meal,user,timeTag,recordDate);

        return meal;

    }

    public MealResponseVO saveMealDetail(Meal meal, List<MealDetailVO> mealDetails) {


        for(MealDetailVO requestDetail : mealDetails){
            MealDetail col = new MealDetail();

            col.setFoodName(requestDetail.getFoodName());
            col.setQuantity(requestDetail.getQuantity());

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

        return MealResponseVO.builder().recordId(saved.getId())
                .mealDetails(detailConverted)
                .timeTag(saved.getTimeTag().getValue())
                .recordDate(saved.getRecordDate())
                .build();

    }

    MealDetailResponseVO mealDetailConvert(MealDetail saved){
        return MealDetailResponseVO.builder().mealDetailId(saved.getId())
                .foodName(saved.getFoodName()).imageUrl(saved.getImageUrl())
                .quantity(saved.getQuantity())
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


    public List<Records> findAllByUser(User user){
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));

        String startTime = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        log.info("Start time : {}, End time : {}",startTime,endTime);

        return recordRepository.findAllByUserAndRecordDateBetween(user,startTime,endTime);
    }

    public List<Records> findAllByUserAndDateAndTimeTagAndRecordType(User user, DateAndTimeTagFilterRequestVO request){

        List<TimeTagCode> timeTagCodes = new ArrayList<>();

        for(String type : request.getTimeTagList()){
            switch (type){
                case "아침":
                    timeTagCodes.add(TimeTagCode.MORNING_BEFORE);
                    timeTagCodes.add(TimeTagCode.MORNING);
                    timeTagCodes.add(TimeTagCode.MORNING_AFTER);
                    break;
                case "점심":
                    timeTagCodes.add(TimeTagCode.NOON_BEFORE);
                    timeTagCodes.add(TimeTagCode.NOON);
                    timeTagCodes.add(TimeTagCode.NOON_AFTER);
                    break;
                case "저녁":
                    timeTagCodes.add(TimeTagCode.NIGHT_BEFORE);
                    timeTagCodes.add(TimeTagCode.NIGHT);
                    timeTagCodes.add(TimeTagCode.NIGHT_AFTER);
                    break;
                default:
                    throw new IllegalStateException(String.format("Not supported type %s",type));
            }
        }

        log.info("Start time : {}, End time : {}", request.getStartDate(),request.getEndDate());

        return recordRepository.findAllByUserAndRecordDateBetweenAndTypeInAndTimeTagInOrderByCreatedDateDesc(user,
                request.getStartDate(), request.getEndDate(),request.getFilterType(), timeTagCodes);
    }

    public Page<Records> findAllByUser(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return recordRepository.findAllByUserOrderByCreatedDateDesc(pageRequest,user);
    }

    public Page<Records> findAllByUser(User user, int page, int size, List<String> filterType) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return recordRepository.findAllByUserAndTypeInOrderByCreatedDateDesc(pageRequest,user,filterType);
    }


    public MedicineResponseVO saveMedicine(MedicineVO requestRecord,String timeTag, String recordDate, User user) {

        Medicine medicine = new Medicine();
        setRecordBase(medicine,user,timeTag,recordDate);
        if(requestRecord.getMedicineId()!=null)
            medicine.setMedicineId(requestRecord.getMedicineId());

        medicine.setMedicineCnt(requestRecord.getMedicineCnt());
        medicine.setMedicineName(requestRecord.getMedicineName());
        Medicine saved = medicineRepository.save(medicine);


        return convertMedicine(saved);
    }

    public MedicineResponseVO convertMedicine(Medicine saved){
        return MedicineResponseVO.builder().medicineId(saved.getMedicineId())
                .recordDate(saved.getRecordDate())
                .recordId(saved.getId()).medicineCnt(saved.getMedicineCnt())
                .medicineName(saved.getMedicineName()).timeTag(saved.getTimeTag().getValue())
                .build();
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
        medicine.setRecordDate(requestRecord.getRecordDate());
        medicine.setTimeTag(TimeTagCode.findByValue(requestRecord.getTimeTag()));

        Medicine saved = medicineRepository.save(medicine);

        return convertMedicine(saved);
    }

    public List<RecordLookUpVO> getRecordList(List<Records> records){
        List<RecordLookUpVO> retValue = new ArrayList<>();

        //record 매핑
        for(Records record : records){
            RecordLookUpVO col = new RecordLookUpVO();
            switch (record.getType()){
                case "SUGAR":
                    col.setType("SUGAR");
                    col.setRecord(bSugarConvert((BSugar) record));
                    break;

                case "PRESSURE":
                    col.setType("PRESSURE");
                    col.setRecord(bPressureConvert((BPressure) record));
                    break;

                case "MEAL":
                    col.setType("MEAL");
                    col.setRecord(mealConvert((Meal) record));
                    break;
                case "MEDICINE":
                    col.setType("MEDICINE");
                    col.setRecord(convertMedicine((Medicine) record));
                    break;
                default:
                    break;
            }

            retValue.add(col);

        }
        return retValue;
    }
}
