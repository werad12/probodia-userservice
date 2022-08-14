package com.probodia.userservice.api.controller.record;


import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.RecordService;
import com.probodia.userservice.api.service.UserService;
import com.probodia.userservice.api.vo.*;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import com.probodia.userservice.utils.PageInfoUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.reflection.XMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/api/record")
@Slf4j
public class RecordController {

    private RecordService recordService;
    private UserService userService;
    private AuthTokenProvider tokenProvider;


    @Autowired
    public RecordController(RecordService recordService, UserService userService, AuthTokenProvider tokenProvider) {
        this.recordService = recordService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }



    @PostMapping("/sugar")
    @ApiOperation(value = "혈당 기록 저장", notes = "혈당 기록을 저장한다.")
    public ResponseEntity<BSugarResponse> saveBSugarRecord(@RequestHeader(value = "Authorization")String token,
                                                           @Valid @RequestBody BSugarVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //혈당 기록 저장
        BSugarResponse saved = recordService.saveSugar(requestRecord, user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/pressure")
    @ApiOperation(value = "혈압 기록 저장", notes = "혈압 기록을 저장한다.")
    public ResponseEntity<BPressureResponse> saveBPressureRecord(@RequestHeader(value = "Authorization")String token,
                                                                 @Valid @RequestBody BPressureVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //혈압 기록 저장
        BPressureResponse saved = recordService.savePressure(requestRecord, user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/meal")
    @ApiOperation(value = "음식 기록 추가", notes = "음식 기록을 추가한다.")
    public ResponseEntity<MealResponseVO> saveMeal(@RequestHeader(value = "Authorization")String token,
                                                   @Valid @RequestBody MealVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //Meal 데이터 먼저 저장
        Meal savedMeal = recordService.saveMeal(user, requestRecord.getTimeTag(),requestRecord.getRecordDate());

        //Meal Detail 저장 + Meal 데이터 일부 수정
        MealResponseVO result = recordService.saveMealDetail(savedMeal, requestRecord.getMealDetails());


        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @PostMapping("/medicine")
    @ApiOperation(value = "투약 기록 저장", notes = "투약 기록을 저장한다.")
    public ResponseEntity<List<MedicineResponseVO>> saveMedicineRecord(@RequestHeader(value = "Authorization")String token,
                                                                 @Valid @RequestBody MedicineListVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);

        List<MedicineResponseVO> ret = new ArrayList<>();
        //투약 기록 저장
        for(MedicineVO request : requestRecord.getMedicineDetails()){
            MedicineResponseVO saved = recordService.saveMedicine(request, requestRecord.getTimeTag(),
                    requestRecord.getRecordDate(), user);
            ret.add(saved);
        }

        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }


    @PutMapping("/sugar")
    @ApiOperation(value = "혈당 기록 수정", notes = "혈당 기록을 수정한다.")
    public ResponseEntity<BSugarResponse> updateBSugarRecord(@RequestHeader(value = "Authorization")String token,
                                                             @Valid @RequestBody BSugarUpdateVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //record 찾기
        Optional<BSugar> updateRecord = recordService.findBSugarByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈당 기록 수정
        BSugarResponse result = recordService.updateBSugar(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/pressure")
    @ApiOperation(value = "혈압 기록 수정", notes = "혈압 기록을 수정한다.")
    public ResponseEntity<BPressureResponse> updateBPressureRecord(@RequestHeader(value = "Authorization")String token,
                                                                   @Valid @RequestBody BPressureUpdateVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //혈압 기록 저장
        Optional<BPressure> updateRecord = recordService.findBPressureByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈압 기록 수정
        BPressureResponse result = recordService.updateBPressure(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @PutMapping("/meal")
    @ApiOperation(value = "음식 기록 수정", notes = "음식 기록을 수정한다.")
    public ResponseEntity<MealResponseVO> updateMeal(@RequestHeader(value = "Authorization")String token,
                                                     @Valid @RequestBody MealUpdateVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //음식 기록 저장
        Optional<Meal> updateRecord = recordService.findMealByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //음식 기록 수정
        MealResponseVO result = recordService.updateMeal(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @PutMapping("/medicine")
    @ApiOperation(value = "투약 기록 수정", notes = "투약 기록을 수정한다.")
    public ResponseEntity<MedicineResponseVO> updateBSugarRecord(@RequestHeader(value = "Authorization")String token,
                                                                 @Valid @RequestBody MedicineUpdateVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //record 찾기
        Optional<Medicine> updateRecord = recordService.findMedicineByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈당 기록 수정
        MedicineResponseVO result = recordService.updateMedicine(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/sugar")
    @ApiOperation(value = "혈당 기록 삭제", notes = "혈당 기록을 삭제한다.")
    public ResponseEntity<Long> deleteBSugarRecord(@RequestHeader(value = "Authorization")String token,
                                                   @Valid @RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUserByToken(token);

        //record 찾기
        Optional<BSugar> deleteRecord = recordService.findBSugarByUserAndId(user, request.getRecordId());
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteBSugar(deleteRecord.get()),HttpStatus.OK);
    }

    @DeleteMapping("/pressure")
    @ApiOperation(value = "혈압 기록 삭제", notes = "혈압 기록을 삭제한다.")
    public ResponseEntity<Long> deleteBPressureRecord(@RequestHeader(value = "Authorization")String token,
                                                      @Valid @RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUserByToken(token);

        //record 찾기
        Optional<BPressure> deleteRecord = recordService.findBPressureByUserAndId(user, request.getRecordId());
        log.info("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteBPressure(deleteRecord.get()),HttpStatus.OK);
    }

    @DeleteMapping("/meal")
    @ApiOperation(value = "음식 기록 삭제", notes = "음식 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMeal(@RequestHeader(value = "Authorization")String token,
                                           @Valid @RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUserByToken(token);
        //log.info("user Id : {}",user.getUserId());

        //record 찾기
        Optional<Meal> deleteRecord = recordService.findMealByUserAndId(user, request.getRecordId());
        log.info("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteMeal(deleteRecord.get()),HttpStatus.OK);
    }



    @DeleteMapping("/medicine")
    @ApiOperation(value = "투약 기록 삭제", notes = "투약 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMedicineRecord(@RequestHeader(value = "Authorization")String token,
                                                     @Valid @RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUserByToken(token);

        //record 찾기
        Optional<Medicine> deleteRecord = recordService.findMedicineByUserAndId(user, request.getRecordId());
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteMedicine(deleteRecord.get()),HttpStatus.OK);
    }




    @GetMapping("/getAll/{page}/{size}")
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "모든 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpVO> getAllRecords(@RequestHeader(value = "Authorization")String token,
                                                              @PathVariable(name = "page") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                                              @NotNull(message = "Page number cannot be null")Integer page,
                                                              @PathVariable(name = "size") @ApiParam(value = "한 페이지의 사이즈", required = true,example = "123")
                                                                  @NotNull(message = "Paging size cannot be null")Integer size
                                                              ){
        //user 찾기
        User user = getUserByToken(token);
        //user에 따른 레코드 찾기
        Page<Records> pageRecord = recordService.findAllByUser(user,page - 1,size);
        PageInfoUtil pageInfo = new PageInfoUtil(page,size,(int) pageRecord.getTotalElements(), pageRecord.getTotalPages());

        List<Records> records = pageRecord.getContent();
        List<RecordLookUpVO> retValue = recordService.getRecordList(records);

        return new ResponseEntity<>(new PagingLookUpVO(retValue,pageInfo),HttpStatus.OK);
    }

    @GetMapping("/getAllFiltered")
    @ApiOperation(value = "user Id로 일부 기록을 가져온다.", notes = "일부 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpVO> getFilteredRecord(@RequestHeader(value = "Authorization")String token,
                                                            @Valid @RequestBody PagingFilterRequestVO request){

        //user 찾기
        User user = getUserByToken(token);

        //user에 따른 레코드 찾기
        Page<Records> pageRecord = recordService.findAllByUser(user,request.getPage() - 1,request.getSize(),request.getFilterType());
        PageInfoUtil pageInfo = new PageInfoUtil(request.getPage(),request.getSize(),(int) pageRecord.getTotalElements(), pageRecord.getTotalPages());

        List<Records> records = pageRecord.getContent();
        List<RecordLookUpVO> retValue = recordService.getRecordList(records);

        return new ResponseEntity<>(new PagingLookUpVO(retValue,pageInfo),HttpStatus.OK);
    }

    @GetMapping("/getAllToday")
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "오늘의 기록을 가져온다.")
    public ResponseEntity<List<RecordLookUpVO>> getAllTodayRecords(@RequestHeader(value = "Authorization")String token){
        //user 찾기
        User user = getUserByToken(token);
        //user에 따른 레코드 찾기
        List<Records> records = recordService.findAllByUser(user);

        return new ResponseEntity<>(recordService.getRecordList(records),HttpStatus.OK);
    }

    @PostMapping("/getAllByDateAndTimeTag")
    @ApiOperation(value = "기간과 timetag(아침, 점심, 저녁)으로 전체 기록을 가져온다.", notes = "timetag에는 아침, 점심, 저녁만 들어갈 수 있고, 혈당의 경우는 아침 -> 아침 식전, 식후 데이터를 모두 가져온다. 페이징은 하지 않는다.")
    public ResponseEntity<List<RecordLookUpVO>> getAllByDateAndTimeTag(@RequestHeader(value = "Authorization")String token,
                                                                       @Valid @RequestBody DateAndTimeTagFilterRequestVO request){
        //user 찾기
        User user = getUserByToken(token);
        //user에 따른 레코드 찾기
        List<Records> records = recordService.findAllByUserAndDateAndTimeTagAndRecordType(user,request);

        return new ResponseEntity<>(recordService.getRecordList(records),HttpStatus.OK);
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
