package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.*;
import com.probodia.userservice.api.vo.bpressure.BPressureResponse;
import com.probodia.userservice.api.vo.bsugar.BSugarResponse;
import com.probodia.userservice.api.vo.meal.MealDetailResponseVO;
import com.probodia.userservice.api.vo.meal.MealResponseVO;
import com.probodia.userservice.api.vo.medicine.MedicineDetailResponseVO;
import com.probodia.userservice.api.vo.medicine.MedicineResponseVO;
import com.probodia.userservice.api.vo.recordbase.RecordLookUpVO;
import com.probodia.userservice.api.vo.recordview.DateAndTimeTagFilterRequestVO;
import com.probodia.userservice.api.vo.recordview.PagingLookUpVO;
import com.probodia.userservice.utils.PageInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class RecordService {


    private RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Transactional(readOnly = true)
    public List<RecordLookUpVO> findAllByUser(User user){
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));

        String startTime = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        log.info("Start time : {}, End time : {}",startTime,endTime);
        List<Records> records = recordRepository.findAllByUserAndRecordDateBetween(user, startTime, endTime);

        return getRecordList(records);
    }

    @Transactional(readOnly = true)
    public List<RecordLookUpVO> findAllByUserAndDateAndTimeTagAndRecordType(User user, DateAndTimeTagFilterRequestVO request){

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
        List<Records> records = recordRepository.findAllByUserAndRecordDateBetweenAndTypeInAndTimeTagInOrderByRecordDateDesc(user,
                request.getStartDate(), request.getEndDate(), request.getFilterType(), timeTagCodes);

        log.info("SIZE : {}", records.size());

        return getRecordList(records);
    }
    @Transactional(readOnly = true)
    public PagingLookUpVO findAllByUser(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Records> pageRecord =recordRepository.findAllByUserOrderByRecordDateDesc(pageRequest,user);
        PageInfoUtil pageInfo = new PageInfoUtil(page,size,(int) pageRecord.getTotalElements(), pageRecord.getTotalPages());

        List<Records> records = pageRecord.getContent();
        List<RecordLookUpVO> retValue = getRecordList(records);
        return new PagingLookUpVO(retValue,pageInfo);
    }
    @Transactional(readOnly = true)
    public PagingLookUpVO findAllByUser(User user, int page, int size, List<String> filterType) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Records> pageRecord = recordRepository.findAllByUserAndTypeInOrderByRecordDateDesc(pageRequest,user,filterType);
        PageInfoUtil pageInfo = new PageInfoUtil(page,size,(int) pageRecord.getTotalElements(), pageRecord.getTotalPages());
        //
        List<Records> records = pageRecord.getContent();
        List<RecordLookUpVO> retValue = getRecordList(records);
        return new PagingLookUpVO(retValue,pageInfo);
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

    private BPressureResponse bPressureConvert(BPressure bPressure){
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

    private BSugarResponse bSugarConvert(BSugar bSugar){
        return
                BSugarResponse.builder().bloodSugar(bSugar.getBloodSugar())
                        .timeTag(bSugar.getTimeTag().getValue())
                        .recordId(bSugar.getId())
                        .recordDate(bSugar.getRecordDate())
                        .build();
    }

    private MealResponseVO mealConvert(Meal saved){
        List<MealDetailResponseVO> detailConverted = new ArrayList<>();
        for(MealDetail detail : saved.getMealDetails()){
            detailConverted.add(mealDetailConvert(detail));
        }

        return MealResponseVO.builder().recordId(saved.getId())
                .mealDetails(detailConverted)
                .timeTag(saved.getTimeTag().getValue())
                .recordDate(saved.getRecordDate()).userId(saved.getUser().getUserId())
                .build();

    }

    private MealDetailResponseVO mealDetailConvert(MealDetail saved){
        return MealDetailResponseVO.builder().mealDetailId(saved.getId())
                .foodName(saved.getFoodName()).imageUrl(saved.getImageUrl())
                .quantity(saved.getQuantity()).foodId(saved.getFoodId())
                .bloodSugar(saved.getBloodSugar()).calories(saved.getCalorie()).build();
    }

    private MedicineResponseVO convertMedicine(Medicine saved){
        List<MedicineDetailResponseVO> detailConverted = new ArrayList<>();
        saved.getMedicineDetails().stream().forEach(s -> detailConverted.add(medicineDetailConvert(s)));

        return MedicineResponseVO.builder().recordId(saved.getId())
                .medicineDetails(detailConverted)
                .timeTag(saved.getTimeTag().getValue())
                .recordDate(saved.getRecordDate())
                .build();
    }

    private  MedicineDetailResponseVO medicineDetailConvert(MedicineDetail saved){
        return MedicineDetailResponseVO.builder().medicineDetailId(saved.getId())
                .medicineCnt(saved.getMedicineCnt())
                .medicineName(saved.getMedicineName())
                .medicineId(saved.getMedicineId())
                .build();
    }

}
