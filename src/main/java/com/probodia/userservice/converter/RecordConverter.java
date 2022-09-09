package com.probodia.userservice.converter;

import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.vo.*;

import java.util.ArrayList;
import java.util.List;

public class RecordConverter {



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
                .quantity(saved.getQuantity()).foodId(saved.getFoodId())
                .bloodSugar(saved.getBloodSugar()).calories(saved.getCalorie()).build();
    }

    public static MedicineResponseVO convertMedicine(Medicine saved){
        List<MedicineDetailResponseVO> detailConverted = new ArrayList<>();
        saved.getMedicineDetails().stream().forEach(s -> detailConverted.add(medicineDetailConvert(s)));

        return MedicineResponseVO.builder().recordId(saved.getId())
                .medicineDetails(detailConverted)
                .timeTag(saved.getTimeTag().getValue())
                .recordDate(saved.getRecordDate())
                .build();
    }

    private static MedicineDetailResponseVO medicineDetailConvert(MedicineDetail saved){
        return MedicineDetailResponseVO.builder().medicineDetailId(saved.getId())
                .medicineCnt(saved.getMedicineCnt())
                .medicineName(saved.getMedicineName())
                .medicineId(saved.getMedicineId())
                .build();
    }
}
