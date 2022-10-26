package com.probodia.userservice.api.controller.record;


import com.probodia.userservice.api.annotation.TokenRequired;
import com.probodia.userservice.api.dto.recordview.DoctorRecordDto;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.RecordService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.dto.recordbase.RecordLookUpDto;
import com.probodia.userservice.api.dto.recordview.DateAndTimeTagFilterRequestDto;
import com.probodia.userservice.api.dto.recordview.PagingFilterRequestDto;
import com.probodia.userservice.api.dto.recordview.PagingLookUpDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/api/record")
@Slf4j
@RequiredArgsConstructor
@Api(value = "Record Controller", description = "사용자 건강 기록 조회와 관련된 API")
public class RecordController {

    private final RecordService recordService;



    @GetMapping("/getAll/{page}/{size}")
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "모든 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpDto> getAllRecords(@TokenRequired User user,
                                                         @PathVariable(name = "page") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                                              @NotNull(message = "Page number cannot be null")Integer page,
                                                         @PathVariable(name = "size") @ApiParam(value = "한 페이지의 사이즈", required = true,example = "123")
                                                                  @NotNull(message = "Paging size cannot be null")Integer size
                                                              ){

        //user에 따른 레코드 찾기

        return new ResponseEntity<>(recordService.findAllByUser(user,page - 1,size),HttpStatus.OK);
    }

    @PostMapping("/getAllFiltered")
    @ApiOperation(value = "user Id로 일부 기록을 가져온다.", notes = "일부 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpDto> getFilteredRecord(@TokenRequired User user,
                                                             @Valid @RequestBody PagingFilterRequestDto request){


        //user에 따른 레코드 찾기
        return new ResponseEntity<>(recordService.findAllByUser(user,request.getPage() - 1,request.getSize(),request.getFilterType()),HttpStatus.OK);

    }

    @PostMapping("/doctor-view")
    @ApiOperation(value = "의사에게 보여주기에 사용할 메서드", notes = "혈당과 혈압 기록을 지정한 요일의 크기만큼 가져오는데, 여기서 pagenation도 같이 한다.")
    public ResponseEntity<PagingLookUpDto> viewDoctor(@TokenRequired User user,
                                                            @RequestBody DoctorRecordDto req){

        for(String type : req.getFilterType()){
            if(!(type.equals("PRESSURE") || type.equals("SUGAR")))
            throw new IllegalArgumentException("Not supported Type!");
        }

        PagingLookUpDto res = recordService.getDocterView(user, req.getPagingSize(),
                req.getSize(), req.getPage(), req.getStartDate(),req.getFilterType());

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/getAllToday")
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "오늘의 기록을 가져온다.")
    @Transactional(readOnly = true)
    public ResponseEntity<List<RecordLookUpDto>> getAllTodayRecords(@TokenRequired User user){

        //user에 따른 레코드 찾기
        return new ResponseEntity<>(recordService.findAllByUser(user),HttpStatus.OK);
    }

    @PostMapping("/getAllByDateAndTimeTag")
    @ApiOperation(value = "기간과 timetag(아침, 점심, 저녁)으로 전체 기록을 가져온다.", notes = "timetag에는 아침, 점심, 저녁만 들어갈 수 있고, 혈당의 경우는 아침 -> 아침 식전, 식후 데이터를 모두 가져온다. 페이징은 하지 않는다.")
    public ResponseEntity<List<RecordLookUpDto>> getAllByDateAndTimeTag(@TokenRequired User user,
                                                                        @Valid @RequestBody DateAndTimeTagFilterRequestDto request){

        return new ResponseEntity<>(recordService.findAllByUserAndDateAndTimeTagAndRecordType(user,request),HttpStatus.OK);
    }

}
