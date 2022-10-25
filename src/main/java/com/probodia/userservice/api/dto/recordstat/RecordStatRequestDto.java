package com.probodia.userservice.api.dto.recordstat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@ApiModel(value = "분석 리포트 요청 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordStatRequestDto {

    @ApiModelProperty(value = "시작 시간", example = "2017-11-12")
    @NotNull(message = "Start time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startDate;

    @ApiModelProperty(value = "끝나는 시간", example = "2017-11-12")
    @NotNull(message = "End time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDate;
}
