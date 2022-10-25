package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.BPressureRepository;
import com.probodia.userservice.api.dto.bpressure.BPressureResponse;
import com.probodia.userservice.api.dto.bpressure.BPressureUpdateDto;
import com.probodia.userservice.api.dto.bpressure.BPressureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class BpressureService {

    private BPressureRepository bPressureRepository;

    @Autowired
    public BpressureService(BPressureRepository bPressureRepository) {
        this.bPressureRepository = bPressureRepository;
    }

    private void setRecordBase(Records record, User user, String timeTag, String recordDate){
        record.setUser(user);
        record.setRecordDate(recordDate);
        record.setTimeTag(TimeTagCode.findByValue(timeTag));

    }

    @Transactional
    public BPressureResponse savePressure(BPressureDto request, User user) {

        BPressure bPressure = new BPressure();
        setRecordBase(bPressure,user,request.getTimeTag(),request.getRecordDate());
        bPressure.setMaxBloodPressure(request.getMaxBloodPressure());
        bPressure.setMinBloodPressure(request.getMinBloodPressure());
        bPressure.setHeartBeat(request.getHeartBeat());
        BPressure saved = bPressureRepository.save(bPressure);

        return bPressureConvert(saved);
    }



    @Transactional
    public BPressureResponse updateBPressure(BPressure bPressure, BPressureUpdateDto requestRecord) {
        bPressure.setMinBloodPressure(requestRecord.getMinBloodPressure());
        bPressure.setMaxBloodPressure(requestRecord.getMaxBloodPressure());
        bPressure.setHeartBeat(requestRecord.getHeartBeat());
        bPressure.setTimeTag(TimeTagCode.findByValue(requestRecord.getTimeTag()));
        bPressure.setRecordDate(requestRecord.getRecordDate());

        BPressure saved = bPressureRepository.save(bPressure);

        return bPressureConvert(saved);
    }
    public Optional<BPressure> findBPressureByUserAndId(User user, Long recordId) {
        return bPressureRepository.findByUserAndId(user,recordId);
    }

    @Transactional
    public Long deleteBPressure(BPressure deleteRecord) {
        bPressureRepository.delete(deleteRecord);
        return deleteRecord.getId();
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
}
