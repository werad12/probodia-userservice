package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.BpressureService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.vo.BPressureResponse;
import com.probodia.userservice.api.vo.BPressureUpdateVO;
import com.probodia.userservice.api.vo.BPressureVO;
import com.probodia.userservice.api.vo.RecordDeleteRequest;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/record/pressure")
@Slf4j
@Api(value = "Blood Pressure Controller", description = "혈압 기과 관련된 API")
public class BPressureController {

    private BpressureService bPressureService;
    private UserService userService;
    private AuthTokenProvider tokenProvider;

    @Autowired
    public BPressureController(BpressureService bPressureService, UserService userService, AuthTokenProvider tokenProvider) {
        this.bPressureService = bPressureService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping
    @ApiOperation(value = "혈압 기록 저장", notes = "혈압 기록을 저장한다.")
    public ResponseEntity<BPressureResponse> saveBPressureRecord(@RequestHeader(value = "Authorization")String token,
                                                                 @Valid @RequestBody BPressureVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //혈압 기록 저장
        BPressureResponse saved = bPressureService.savePressure(requestRecord, user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @PutMapping
    @ApiOperation(value = "혈압 기록 수정", notes = "혈압 기록을 수정한다.")
    public ResponseEntity<BPressureResponse> updateBPressureRecord(@RequestHeader(value = "Authorization")String token,
                                                                   @Valid @RequestBody BPressureUpdateVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //혈압 기록 저장
        Optional<BPressure> updateRecord = bPressureService.findBPressureByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈압 기록 수정
        BPressureResponse result = bPressureService.updateBPressure(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @DeleteMapping
    @ApiOperation(value = "혈압 기록 삭제", notes = "혈압 기록을 삭제한다.")
    public ResponseEntity<Long> deleteBPressureRecord(@RequestHeader(value = "Authorization")String token,
                                                      @Valid @RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUserByToken(token);

        //record 찾기
        Optional<BPressure> deleteRecord = bPressureService.findBPressureByUserAndId(user, request.getRecordId());
        log.info("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(bPressureService.deleteBPressure(deleteRecord.get()),HttpStatus.OK);
    }

    private User getUserByToken(String bearerToken){

        return getUser(tokenProvider.getTokenSubject(bearerToken.substring(7)));
    }

    private User getUser(Long userId){
        return getUser(String.valueOf(userId));
    }

    private User getUser(String userId) {
        User user = userService.getUser(userId);

        if(user==null){
            throw new UsernameNotFoundException("Not found User by userId");
        }
        return user;
    }
}
