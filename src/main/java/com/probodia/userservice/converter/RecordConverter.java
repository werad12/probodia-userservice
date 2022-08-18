package com.probodia.userservice.converter;

import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.vo.*;

import java.util.ArrayList;
import java.util.List;

public class RecordConverter {

    public static MedicineResponseVO convertMedicine(Medicine saved){
        return MedicineResponseVO.builder().medicineId(saved.getMedicineId())
                .recordDate(saved.getRecordDate())
                .recordId(saved.getId()).medicineCnt(saved.getMedicineCnt())
                .medicineName(saved.getMedicineName()).timeTag(saved.getTimeTag().getValue())
                .build();
    }

    public static BPressureResponse bPressureConvert(BPressure bPressure){
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

    public static BSugarResponse bSugarConvert(BSugar bSugar){
        return
                BSugarResponse.builder().bloodSugar(bSugar.getBloodSugar())
                        .timeTag(bSugar.getTimeTag().getValue())
                        .recordId(bSugar.getId())
                        .recordDate(bSugar.getRecordDate())
                        .build();
    }

    public static MealResponseVO mealConvert(Meal saved){
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

    private static MealDetailResponseVO mealDetailConvert(MealDetail saved){
        return MealDetailResponseVO.builder().mealDetailId(saved.getId())
                .foodName(saved.getFoodName()).imageUrl(saved.getImageUrl())
                .quantity(saved.getQuantity())
                .bloodSugar(saved.getBloodSugar()).calories(saved.getCalorie()).build();
    }
}