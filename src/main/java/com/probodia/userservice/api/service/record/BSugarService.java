package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.BSugar;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.BSugarRepository;
import com.probodia.userservice.api.dto.bsugar.BSugarResponse;
import com.probodia.userservice.api.dto.bsugar.BSugarUpdateDto;
import com.probodia.userservice.api.dto.bsugar.BSugarDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Slf4j
public class BSugarService {

    private BSugarRepository bSugarRepository;

    @Autowired
    public BSugarService(BSugarRepository bSugarRepository) {
        this.bSugarRepository = bSugarRepository;
    }

    private void setRecordBase(Records record, User user, String timeTag, String recordDate){
        record.setUser(user);
        record.setRecordDate(recordDate);
        record.setTimeTag(TimeTagCode.findByValue(timeTag));

    }

    @Transactional
    public BSugarResponse saveSugar(BSugarDto request, User user) {

        BSugar bSugar = new BSugar();
        setRecordBase(bSugar,user,request.getTimeTag(),request.getRecordDate());
        bSugar.setBloodSugar(request.getBloodSugar());

        BSugar saved = bSugarRepository.save(bSugar);

        return bSugarConvert(saved);
    }



    @Transactional
    public BSugarResponse updateBSugar(BSugar sugar, BSugarUpdateDto updateVO){
        sugar.setBloodSugar(updateVO.getBloodSugar());
        sugar.setTimeTag(TimeTagCode.findByValue(updateVO.getTimeTag()));
        sugar.setRecordDate(updateVO.getRecordDate());

        BSugar saved = bSugarRepository.save(sugar);

        return bSugarConvert(saved);
    }



    public Optional<BSugar> findBSugarByUserAndId(User user, Long recordId) {
        return bSugarRepository.findByUserAndId(user,recordId);
    }

    @Transactional
    public Long deleteBSugar(BSugar deleteRecord) {
        bSugarRepository.delete(deleteRecord);
        return deleteRecord.getId();
    }

    private BSugarResponse bSugarConvert(BSugar bSugar){
        return
                BSugarResponse.builder().bloodSugar(bSugar.getBloodSugar())
                        .timeTag(bSugar.getTimeTag().getValue())
                        .recordId(bSugar.getId())
                        .recordDate(bSugar.getRecordDate()).userId(bSugar.getUser().getUserId())
                        .build();
    }

}
