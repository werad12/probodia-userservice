package com.probodia.userservice.api.controller.record;


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
@Api(value = "Record Controller", description = "사용자 건강 기록 조회와 관련된 API")
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

    @GetMapping("/getAll/{page}/{size}")
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "모든 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpDto> getAllRecords(@RequestHeader(value = "Authorization")String token,
                                                         @PathVariable(name = "page") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                                              @NotNull(message = "Page number cannot be null")Integer page,
                                                         @PathVariable(name = "size") @ApiParam(value = "한 페이지의 사이즈", required = true,example = "123")
                                                                  @NotNull(message = "Paging size cannot be null")Integer size
                                                              ){
        //user 찾기
        User user = getUserByToken(token);

        //user에 따른 레코드 찾기

        return new ResponseEntity<>(recordService.findAllByUser(user,page - 1,size),HttpStatus.OK);
    }

    @PostMapping("/getAllFiltered")
    @ApiOperation(value = "user Id로 일부 기록을 가져온다.", notes = "일부 기록을 가져온다. 페이징 번호는 1부터 시작한다.")
    public ResponseEntity<PagingLookUpDto> getFilteredRecord(@RequestHeader(value = "Authorization")String token,
                                                             @Valid @RequestBody PagingFilterRequestDto request){

        //user 찾기
        User user = getUserByToken(token);

        //user에 따른 레코드 찾기
        return new ResponseEntity<>(recordService.findAllByUser(user,request.getPage() - 1,request.getSize(),request.getFilterType()),HttpStatus.OK);

    }

    @GetMapping("/getAllToday")
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "오늘의 기록을 가져온다.")
    @Transactional(readOnly = true)
    public ResponseEntity<List<RecordLookUpDto>> getAllTodayRecords(@RequestHeader(value = "Authorization")String token){
        //user 찾기
        User user = getUserByToken(token);
        //user에 따른 레코드 찾기

        return new ResponseEntity<>(recordService.findAllByUser(user),HttpStatus.OK);
    }

    @PostMapping("/getAllByDateAndTimeTag")
    @ApiOperation(value = "기간과 timetag(아침, 점심, 저녁)으로 전체 기록을 가져온다.", notes = "timetag에는 아침, 점심, 저녁만 들어갈 수 있고, 혈당의 경우는 아침 -> 아침 식전, 식후 데이터를 모두 가져온다. 페이징은 하지 않는다.")
    public ResponseEntity<List<RecordLookUpDto>> getAllByDateAndTimeTag(@RequestHeader(value = "Authorization")String token,
                                                                        @Valid @RequestBody DateAndTimeTagFilterRequestDto request){
        //user 찾기
        User user = getUserByToken(token);

        return new ResponseEntity<>(recordService.findAllByUserAndDateAndTimeTagAndRecordType(user,request),HttpStatus.OK);
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
