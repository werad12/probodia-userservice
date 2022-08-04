package com.probodia.userservice.api.controller.record;


import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.RecordService;
import com.probodia.userservice.api.service.UserService;
import com.probodia.userservice.api.vo.*;
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

import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/api/record")
@Slf4j
public class RecordController {

    private RecordService recordService;
    private UserService userService;

    @Autowired
    public RecordController(RecordService recordService, UserService userService) {
        this.recordService = recordService;
        this.userService = userService;
    }

    @PostMapping("/sugar")
    @ApiOperation(value = "혈당 기록 저장", notes = "혈당 기록을 저장한다.")
    public ResponseEntity<BSugarResponse> saveBSugarRecord(@RequestBody BSugarVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //혈당 기록 저장
        BSugarResponse saved = recordService.saveSugar(requestRecord.getTimeTag(),requestRecord.getBloodSugar(), user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/pressure")
    @ApiOperation(value = "혈압 기록 저장", notes = "혈압 기록을 저장한다.")
    public ResponseEntity<BPressureResponse> saveBPressureRecord(@RequestBody BPressureVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //혈압 기록 저장
        BPressureResponse saved = recordService.savePressure(requestRecord.getTimeTag(),requestRecord.getBloodPressure(), user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/sugar")
    @ApiOperation(value = "혈당 기록 삭제", notes = "혈당 기록을 삭제한다.")
    public ResponseEntity<Long> deleteBSugarRecord(@RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUser(request.getUserId());


        //record 찾기
        Optional<BSugar> deleteRecord = recordService.findBSugarByUserAndId(user, request.getRecordId());
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteBSugar(deleteRecord.get()),HttpStatus.OK);
    }

    @DeleteMapping("/pressure")
    @ApiOperation(value = "혈압 기록 삭제", notes = "혈압 기록을 삭제한다.")
    public ResponseEntity<Long> deleteBPressureRecord(@RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUser(request.getUserId());

        log.info("user Id : {}",user.getUserId());

        //record 찾기
        Optional<BPressure> deleteRecord = recordService.findBPressureByUserAndId(user, request.getRecordId());
        log.info("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteBPressure(deleteRecord.get()),HttpStatus.OK);
    }

    @PutMapping("/sugar")
    @ApiOperation(value = "혈당 기록 수정", notes = "혈당 기록을 수정한다.")
    public ResponseEntity<BSugarResponse> updateBSugarRecord(@RequestBody BSugarUpdateVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //record 찾기
        Optional<BSugar> updateRecord = recordService.findBSugarByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈당 기록 수정
        BSugarResponse result = recordService.updateBSugar(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/pressure")
    @ApiOperation(value = "혈압 기록 수정", notes = "혈압 기록을 수정한다.")
    public ResponseEntity<BPressureResponse> updateBPressureRecord(@RequestBody BPressureUpdateVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //혈압 기록 저장
        Optional<BPressure> updateRecord = recordService.findBPressureByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈압 기록 수정
        BPressureResponse result = recordService.updateBPressure(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/meal")
    @ApiOperation(value = "음식 기록 추가", notes = "음식 기록을 추가한다.")
    public ResponseEntity<MealResponseVO> saveMeal(@RequestBody MealVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //Meal 데이터 먼저 저장
        Meal savedMeal = recordService.saveMeal(user, requestRecord.getTimeTag());

        //Meal Detail 저장 + Meal 데이터 일부 수정
        MealResponseVO result = recordService.saveMealDetail(savedMeal, requestRecord.getMealDetails());


        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @DeleteMapping("/meal")
    @ApiOperation(value = "음식 기록 삭제", notes = "음식 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMeal(@RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUser(request.getUserId());

        //log.info("user Id : {}",user.getUserId());

        //record 찾기
        Optional<Meal> deleteRecord = recordService.findMealByUserAndId(user, request.getRecordId());
        log.info("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteMeal(deleteRecord.get()),HttpStatus.OK);
    }

    @PutMapping("/meal")
    @ApiOperation(value = "음식 기록 수정", notes = "음식 기록을 수정한다.")
    public ResponseEntity<MealResponseVO> updateMeal(@RequestBody MealUpdateVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //음식 기록 저장
        Optional<Meal> updateRecord = recordService.findMealByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //음식 기록 수정
        MealResponseVO result = recordService.updateMeal(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/medicine")
    @ApiOperation(value = "투약 기록 저장", notes = "투약 기록을 저장한다.")
    public ResponseEntity<MedicineResponseVO> saveMedicineRecord(@RequestBody MedicineVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //투약 기록 저장
        MedicineResponseVO saved = recordService.saveMedicine(requestRecord, user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/medicine")
    @ApiOperation(value = "투약 기록 삭제", notes = "투약 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMedicineRecord(@RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUser(request.getUserId());


        //record 찾기
        Optional<Medicine> deleteRecord = recordService.findMedicineByUserAndId(user, request.getRecordId());
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(recordService.deleteMedicine(deleteRecord.get()),HttpStatus.OK);
    }

    @PutMapping("/medicine")
    @ApiOperation(value = "투약 기록 수정", notes = "투약 기록을 수정한다.")
    public ResponseEntity<MedicineResponseVO> updateBSugarRecord(@RequestBody MedicineUpdateVO requestRecord){

        //user 찾기
        User user = getUser(requestRecord.getUserId());

        //record 찾기
        Optional<Medicine> updateRecord = recordService.findMedicineByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈당 기록 수정
        MedicineResponseVO result = recordService.updateMedicine(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @GetMapping("/getAll/{page}/{size}")
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "모든 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpVO> getAllRecords(@RequestBody @ApiParam(value = "유저 ID", required = true,example = "123123")
                                                          @NotNull(message = "User Id cannot be null")Long userId,
                                                              @PathVariable(name = "page") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                                              @NotNull(message = "Page number cannot be null")Integer page,
                                                              @PathVariable(name = "size") @ApiParam(value = "한 페이지의 사이즈", required = true,example = "123")
                                                                  @NotNull(message = "Paging size cannot be null")Integer size
                                                              ){
        //user 찾기
        User user = getUser(userId);

        //user에 따른 레코드 찾기
        Page<Records> pageRecord = recordService.findAllByUser(user,page - 1,size);
        PageInfoUtil pageInfo = new PageInfoUtil(page,size,(int) pageRecord.getTotalElements(), pageRecord.getTotalPages());

        List<Records> records = pageRecord.getContent();
        List<RecordLookUpVO> retValue = recordService.getRecordList(records);

        return new ResponseEntity<>(new PagingLookUpVO(retValue,pageInfo),HttpStatus.OK);
    }

    @GetMapping("/getAllFiltered")
    @ApiOperation(value = "user Id로 일부 기록을 가져온다.", notes = "일부 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpVO> getFilteredRecord(@RequestBody PagingFilterRequestVO request){

        //user 찾기
        User user = getUser(request.getUserId());

        //user에 따른 레코드 찾기
        Page<Records> pageRecord = recordService.findAllByUser(user,request.getPage() - 1,request.getSize(),request.getFilterType());
        PageInfoUtil pageInfo = new PageInfoUtil(request.getPage(),request.getSize(),(int) pageRecord.getTotalElements(), pageRecord.getTotalPages());

        List<Records> records = pageRecord.getContent();
        List<RecordLookUpVO> retValue = recordService.getRecordList(records);

        return new ResponseEntity<>(new PagingLookUpVO(retValue,pageInfo),HttpStatus.OK);
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
