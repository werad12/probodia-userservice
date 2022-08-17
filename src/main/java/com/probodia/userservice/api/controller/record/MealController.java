package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.MealService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.vo.MealResponseVO;
import com.probodia.userservice.api.vo.MealUpdateVO;
import com.probodia.userservice.api.vo.MealVO;
import com.probodia.userservice.api.vo.RecordDeleteRequest;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
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
@RequestMapping("/api/record/meal")
@Slf4j
public class MealController {

    private MealService mealService;
    private UserService userService;
    private AuthTokenProvider tokenProvider;

    @Autowired
    public MealController(MealService mealService, UserService userService, AuthTokenProvider tokenProvider) {
        this.mealService = mealService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping
    @ApiOperation(value = "음식 기록 추가", notes = "음식 기록을 추가한다.")
    public ResponseEntity<MealResponseVO> saveMeal(@RequestHeader(value = "Authorization")String token,
                                                   @Valid @RequestBody MealVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //Meal 데이터 먼저 저장
        Meal savedMeal = mealService.saveMeal(user, requestRecord.getTimeTag(),requestRecord.getRecordDate());

        //Meal Detail 저장 + Meal 데이터 일부 수정
        MealResponseVO result = mealService.saveMealDetail(savedMeal, requestRecord.getMealDetails());


        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    
    @PutMapping
    @ApiOperation(value = "음식 기록 수정", notes = "음식 기록을 수정한다.")
    public ResponseEntity<MealResponseVO> updateMeal(@RequestHeader(value = "Authorization")String token,
                                                     @Valid @RequestBody MealUpdateVO requestRecord){

        //user 찾기
        User user = getUserByToken(token);
        //음식 기록 저장
        Optional<Meal> updateRecord = mealService.findMealByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //음식 기록 수정
        MealResponseVO result = mealService.updateMeal(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping
    @ApiOperation(value = "음식 기록 삭제", notes = "음식 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMeal(@RequestHeader(value = "Authorization")String token,
                                           @Valid @RequestBody RecordDeleteRequest request){

        //user 찾기
        User user = getUserByToken(token);
        //log.info("user Id : {}",user.getUserId());

        //record 찾기
        Optional<Meal> deleteRecord = mealService.findMealByUserAndId(user, request.getRecordId());
        log.info("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(mealService.deleteMeal(deleteRecord.get()),HttpStatus.OK);
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
