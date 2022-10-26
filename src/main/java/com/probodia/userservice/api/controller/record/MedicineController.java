package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.annotation.TokenRequired;
import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.MedicineService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.dto.medicine.MedicineResponseDto;
import com.probodia.userservice.api.dto.medicine.MedicineUpdateDto;
import com.probodia.userservice.api.dto.medicine.MedicineDto;
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
@RequestMapping("/api/record/medicine")
@Slf4j
@RequiredArgsConstructor
@Api(value = "Medicine Controller", description = "투약 기록과 관련된 API")
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    @ApiOperation(value = "투약 기록 저장", notes = "투약 기록을 저장한다.")
    public ResponseEntity<MedicineResponseDto> saveMedicineRecord(@TokenRequired User user,
                                                                  @Valid @RequestBody MedicineDto requestRecord){

        //투약 데이터 먼저 저장
        //Medicine detail 저장
        MedicineResponseDto savedMedicine = medicineService.saveMedicine(user, requestRecord);

        return new ResponseEntity<>(savedMedicine, HttpStatus.CREATED);
    }


    @PostMapping("/update")
    @ApiOperation(value = "투약 기록 수정", notes = "투약 기록을 수정한다.")
    public ResponseEntity<MedicineResponseDto> updateBSugarRecord(@TokenRequired User user,
                                                                  @Valid @RequestBody MedicineUpdateDto requestRecord){

        //record 찾기
        Optional<Medicine> updateRecord = medicineService.findMedicineByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //혈당 기록 수정
        MedicineResponseDto result = medicineService.updateMedicine(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{recordId}")
    @ApiOperation(value = "투약 기록 삭제", notes = "투약 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMedicineRecord(@TokenRequired User user,
                                                     @PathVariable(name = "recordId") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                                     @NotNull(message = "Record Id cannot be null")Long recordId){


        //record 찾기
        Optional<Medicine> deleteRecord = medicineService.findMedicineByUserAndId(user, recordId);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(medicineService.deleteMedicine(deleteRecord.get()),HttpStatus.OK);
    }


}
