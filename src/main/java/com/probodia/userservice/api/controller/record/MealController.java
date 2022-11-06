package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.annotation.TokenRequired;
import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.MealService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.dto.meal.MealResponseDto;
import com.probodia.userservice.api.dto.meal.MealUpdateDto;
import com.probodia.userservice.api.dto.meal.MealDto;
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
@RequestMapping("/api/record/meal")
@Slf4j
@RequiredArgsConstructor
@Api(value = "Meal Controller", description = "식사 기록과 관련된 API")
public class MealController {

    private final MealService mealService;
    private final RabbitProducer rabbitProducer;


    @PostMapping
    @ApiOperation(value = "음식 기록 추가 api", notes = "음식 기록을 추가한다.")
    public ResponseEntity<MealResponseDto> saveMeal(@TokenRequired User user,
                                                    @Valid @RequestBody MealDto requestRecord){

        //Meal 데이터 먼저 저장
        //Meal Detail 저장 + Meal 데이터 일부 수정
        MealResponseDto savedMeal = mealService.saveMeal(user, requestRecord);


        rabbitProducer.sendFood(savedMeal);

        return new ResponseEntity<>(savedMeal, HttpStatus.CREATED);
    }
    
    @PostMapping("/update")
    @ApiOperation(value = "음식 기록 수정", notes = "음식 기록을 수정한다.")
    public ResponseEntity<MealResponseDto> updateMeal(@TokenRequired User user,
                                                      @Valid @RequestBody MealUpdateDto requestRecord){

        //음식 기록 찾기
        Optional<Meal> updateRecord = mealService.findMealByUserAndId(user, requestRecord.getRecordId());
        if(updateRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        //음식 기록 수정
        MealResponseDto result = mealService.updateMeal(updateRecord.get(), requestRecord);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{recordId}")
    @ApiOperation(value = "음식 기록 삭제", notes = "음식 기록을 삭제한다.")
    public ResponseEntity<Long> deleteMeal(@TokenRequired User user,
                                           @PathVariable(name = "recordId") @ApiParam(value = "페이지 번호", required = true,example = "12")
                                           @NotNull(message = "Record Id cannot be null")Long recordId){

        //record 찾기
        Optional<Meal> deleteRecord = mealService.findMealByUserAndId(user, recordId);
        log.debug("delete Record : {}",deleteRecord);
        if(deleteRecord.isEmpty()) throw new NoSuchElementException("Cannot find record with userId and recordId");

        return new ResponseEntity<>(mealService.deleteMeal(deleteRecord.get()),HttpStatus.OK);
    }
    
    
}
