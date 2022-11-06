package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.annotation.TokenRequired;
import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.BpressureService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.dto.bpressure.BPressureResponse;
import com.probodia.userservice.api.dto.bpressure.BPressureUpdateDto;
import com.probodia.userservice.api.dto.bpressure.BPressureDto;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/record/pressure")
@Slf4j
@RequiredArgsConstructor
@Api(value = "Blood Pressure Controller", description = "혈압 기과 관련된 API")
public class BPressureController {

    private final BpressureService bPressureService;


    @PostMapping
    @ApiOperation(value = "혈압 기록 저장", notes = "혈압 기록을 저장한다.")
    public ResponseEntity<BPressureResponse> saveBPressureRecord(@TokenRequired User user,
                                                                 @Valid @RequestBody BPressureDto requestRecord){

        //혈압 기록 저장
        BPressureResponse saved = bPressureService.savePressure(requestRecord, user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @PutMapping
    @ApiOperation(value = "혈압 기록 수정", notes = "혈압 기록을 수정한다.")
    public ResponseEntity<BPressureResponse> updateBPressureRecord(@TokenRequired User user,
                                                                   @Valid @RequestBody BPressureUpdateDto requestRecord){

        //혈압 기록 저장
        Optional<BPressure> updateRecord = bPressureService.findBPressureByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈압 기록 수정
        BPressureResponse result = bPressureService.updateBPressure(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @DeleteMapping("/{recordId}")
    @ApiOperation(value = "혈압 기록 삭제", notes = "혈압 기록을 삭제한다.")
    public ResponseEntity<Long> deleteBPressureRecord(@TokenRequired User user,
                                                      @PathVariable(name = "recordId") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                                      @NotNull(message = "Record Id cannot be null")Long recordId){

        //record 찾기
        Optional<BPressure> deleteRecord = bPressureService.findBPressureByUserAndId(user, recordId);
        log.debug("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(bPressureService.deleteBPressure(deleteRecord.get()),HttpStatus.OK);
    }

}
