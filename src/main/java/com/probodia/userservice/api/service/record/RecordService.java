package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.*;
import com.probodia.userservice.api.vo.*;
import com.probodia.userservice.converter.RecordConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.probodia.userservice.converter.RecordConverter.*;

@Service
@Slf4j
public class RecordService {


    private RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
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
