package com.probodia.userservice.api.dto.recordview;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "날짜 + timetag 필터 기록 조회 요청 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateAndTimeTagFilterRequestDto {

    @ApiParam(value = "필터링할 타입들", required = true,example = "MEAL")
    @NotNull(message = "Filter type cannot be null")
    List<String> filterType;

    @ApiParam(value = "가져올 timetag", required = true,example = "아침")
    @NotNull(message = "Filter type cannot be null")
    List<String> timeTagList;

    @ApiModelProperty(value = "시작 시간", example = "2017-11-12 13:11:34")
    @NotNull(message = "Start time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startDate;

    @ApiModelProperty(value = "끝나는 시간", example = "2017-11-12 13:11:34")
    @NotNull(message = "End time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endDate;




}
