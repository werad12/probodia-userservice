package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.MedicineService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.vo.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/record/medicine")
@Slf4j
@Api(value = "Medicine Controller", description = "투약 기록과 관련된 API")
public class MedicineController {

    private MedicineService medicineService;
    private UserService userService;
    private AuthTokenProvider tokenProvider;

    @Autowired
    public MedicineController(MedicineService medicineService, UserService userService, AuthTokenProvider tokenProvider) {
        this.medicineService = medicineService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping
    @ApiOperation(value = "투약 기록 저장", notes = "투약 기록을 저장한다.")
    public ResponseEntity<MedicineResponseVO> saveMedicineRecord(@RequestHeader(value = "Authorization")String token,
                                                                       @Valid @RequestBody MedicineVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);

        //투약 데이터 먼저 저장
        Medicine savedMedicine = medicineService.saveMedicine(user, requestRecord.getTimeTag(), requestRecord.getRecordDate());

        //Medicine detail 저장
        MedicineResponseVO result = medicineService.saveMedicineDetail(savedMedicine,requestRecord.getMedicineDetails());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @PutMapping
    @ApiOperation(value = "투약 기록 수정", notes = "투약 기록을 수정한다.")
    public ResponseEntity<MedicineResponseVO> updateBSugarRecord(@RequestHeader(value = "Authorization")String token,
                                                                 @Valid @RequestBody MedicineUpdateVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //record 찾기
        Optional<Medicine> updateRecord = medicineService.findMedicineByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈당 기록 수정
        MedicineResponseVO result = medicineService.updateMedicine(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping
    @ApiOperation(value = "투약 기록 삭제", notes = "투약 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMedicineRecord(@RequestHeader(value = "Authorization")String token,
                                                     @Valid @RequestBody RecordDeleteRequest request){

        //수정
        //user 찾기
        User user = getUserByToken(token);

        //record 찾기
        Optional<Medicine> deleteRecord = medicineService.findMedicineByUserAndId(user, request.getRecordId());
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(medicineService.deleteMedicine(deleteRecord.get()),HttpStatus.OK);
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
