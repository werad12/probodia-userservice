package com.probodia.userservice.api.dto.recordview;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "혈당 또는 혈압 + 기록 있는 날 size 조회 요청 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorRecordDto {
    @ApiParam(value = "필터링할 타입들", required = true,example = "MEAL")
    @NotNull(message = "Filter type cannot be null")
    List<String> filterType;

    @ApiParam(value = "가져올 요일 크기", required = true,example = "10")
    @NotNull(message = "Size cannot be null")
    Integer size;

    @ApiParam(value = "페이지 번호", required = true,example = "10")
    @NotNull(message = "Page number cannot be null")
    Integer page;

    @ApiParam(value = "페이징 크기", required = true,example = "20")
    @NotNull(message = "Size cannot be null")
    Integer pagingSize;

    @ApiModelProperty(value = "시작 시간", example = "2017-11-12 13:11:34")
    @NotNull(message = "Start time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startDate;


}
