package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.annotation.TokenRequired;
import com.probodia.userservice.api.entity.record.BSugar;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.BSugarService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.dto.bsugar.BSugarResponse;
import com.probodia.userservice.api.dto.bsugar.BSugarUpdateDto;
import com.probodia.userservice.api.dto.bsugar.BSugarDto;
import com.probodia.userservice.messagequeue.RabbitProducer;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/record/sugar")
@Slf4j
@RequiredArgsConstructor
@Api(value = "Blood Sugar Controller", description = "혈당 기록과 관련된 API")
public class BSugarController {


    private final BSugarService bSugarService;
    private final RabbitProducer rabbitProducer;
    @PostMapping
    @ApiOperation(value = "혈당 기록 저장", notes = "혈당 기록을 저장한다.")
    public ResponseEntity<BSugarResponse> saveBSugarRecord(@TokenRequired User user,
                                                           @Valid @RequestBody BSugarDto requestRecord){

        //혈당 기록 저장
        BSugarResponse saved = bSugarService.saveSugar(requestRecord, user);


        rabbitProducer.sendSugar(saved);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation(value = "혈당 기록 수정", notes = "혈당 기록을 수정한다.")
    public ResponseEntity<BSugarResponse> updateBSugarRecord(@TokenRequired User user,
                                                             @Valid @RequestBody BSugarUpdateDto requestRecord){

        //record 찾기
        Optional<BSugar> updateRecord = bSugarService.findBSugarByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈당 기록 수정
        BSugarResponse result = bSugarService.updateBSugar(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{recordId}")
    @ApiOperation(value = "혈당 기록 삭제", notes = "혈당 기록을 삭제한다.")
    public ResponseEntity<Long> deleteBSugarRecord(@TokenRequired User user,
                                                   @PathVariable(name = "recordId") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                                   @NotNull(message = "Record Id cannot be null")Long recordId){

        //record 찾기
        Optional<BSugar> deleteRecord = bSugarService.findBSugarByUserAndId(user, recordId);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(bSugarService.deleteBSugar(deleteRecord.get()),HttpStatus.OK);
    }

    
}
