package com.probodia.userservice.api.controller.record;

import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.record.RecordStatisticService;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.vo.recordstat.AverageNeutrientVO;
import com.probodia.userservice.api.vo.recordstat.MedicineStatVO;
import com.probodia.userservice.api.vo.recordstat.RangeBSugarVO;
import com.probodia.userservice.api.vo.recordstat.RecordPercentVO;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recordStat")
@Api(value = "Record Statistic Controller", description = "기록 분석과 관련된 API")
public class RecordStatisticController {

    private final RecordStatisticService recordStatisticService;
    private final UserService userService;
    private final AuthTokenProvider tokenProvider;

    //date 에 대한 validation check 해야 한다.
    @GetMapping("/rangeBsugar/{stdate}/{endate}")
    @ApiOperation(value = "혈당 기록 api", notes = "범위 혈당 분석 정보를 가져온다.")
    public ResponseEntity<RangeBSugarVO> getBsugarRange(@RequestHeader(value = "Authorization")String token,
                                                        @PathVariable @ApiParam(value = "시작 시간", example = "2017-11-12")
                                                            @NotNull(message = "Start time cannot be null")
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid String stdate,
                                                        @PathVariable @ApiParam(value = "끝나는 시간", example = "2017-11-12")
                                                        @NotNull(message = "End time cannot be null")
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid String endate){

        stdate += " 00:00:00";
        endate += " 23:59:59";
        User user = getUserByToken(token);
        RangeBSugarVO ret = recordStatisticService.getBSugarRange(user,stdate,endate);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @GetMapping("/average-nutrient/{stdate}/{endate}")
    @ApiOperation(value = "음식 영양소 분석 api", notes = "음식 데이터 기반 영양소를 가져온다.")
    public ResponseEntity<AverageNeutrientVO> getAverageNutrient(@RequestHeader(value = "Authorization")String token,
                                                                 @PathVariable @ApiParam(value = "시작 시간", example = "2017-11-12")
                                                                 @NotNull(message = "Start time cannot be null")
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid String stdate,
                                                                 @PathVariable @ApiParam(value = "끝나는 시간", example = "2017-11-12")
                                                                     @NotNull(message = "End time cannot be null")
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid String endate){

        stdate += " 00:00:00";
        endate += " 23:59:59";
        User user = getUserByToken(token);
        AverageNeutrientVO ret = recordStatisticService.getAverageNutrient(user,stdate,endate);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @GetMapping("/record-percent/{stdate}/{endate}")
    @ApiOperation(value = "기록 비율 api", notes = "기록을 얼마나 성실히 했는지 가져온다.")
    public ResponseEntity<RecordPercentVO> getRecordPercent(@RequestHeader(value = "Authorization")String token,
                                                            @PathVariable @ApiParam(value = "시작 시간", example = "2017-11-12")
                                                            @NotNull(message = "Start time cannot be null")
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid String stdate,
                                                            @PathVariable @ApiParam(value = "끝나는 시간", example = "2017-11-12") @Valid String endate) throws ParseException {

        stdate += " 00:00:00";
        endate += " 23:59:59";
        User user = getUserByToken(token);
        RecordPercentVO ret = recordStatisticService.getRecordPercent(user,stdate,endate);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @GetMapping("/medicine-stat/{stdate}/{endate}")
    @ApiOperation(value = "투약 기록 분석 api", notes = "무슨 약을 얼마나 먹었는지 가져온다.")
    public ResponseEntity<MedicineStatVO> getMedicineStat(@RequestHeader(value = "Authorization")String token,
                                                          @PathVariable @ApiParam(value = "시작 시간", example = "2017-11-12")
                                                            @NotNull(message = "Start time cannot be null")
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid String stdate,
                                                          @PathVariable @ApiParam(value = "끝나는 시간", example = "2017-11-12") @Valid String endate){

        stdate += " 00:00:00";
        endate += " 23:59:59";
        User user = getUserByToken(token);
        MedicineStatVO ret = recordStatisticService.getMedicineStat(user,stdate,endate);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }


    //당화혈색소 : (혈당 + 46.7) / 28.7
    @GetMapping("/hemoglobin/{stdate}/{endate}")
    @ApiOperation(value = "당화혈색소 api", notes = "당화혈색소를 계산해서 가져온다.")
    public ResponseEntity<Double> getHemoglobin(@RequestHeader(value = "Authorization")String token,
                                                          @PathVariable @ApiParam(value = "시작 시간", example = "2017-11-12")
                                                          @NotNull(message = "Start time cannot be null")
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid String stdate,
                                                          @PathVariable @ApiParam(value = "끝나는 시간", example = "2017-11-12") @Valid String endate){

        stdate += " 00:00:00";
        endate += " 23:59:59";
        User user = getUserByToken(token);

        Double ret = recordStatisticService.getHemoglobin(user,stdate,endate);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    private User getUserByToken(String bearerToken){

        return getUser(tokenProvider.getTokenSubject(bearerToken.substring(7)));
    }
    private User getUser(String userId) {
        User user = userService.getUser(userId);

        if(user==null){
            throw new UsernameNotFoundException("Not found User by userId");
        }
        return user;
    }
}
