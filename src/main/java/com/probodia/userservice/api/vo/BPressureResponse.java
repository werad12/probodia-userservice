package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "혈압 기록 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BPressureResponse {

    @ApiModelProperty(value = "시간 태그",example = "아침")
    private String timeTag;

    @ApiModelProperty(value = "최대 혈압 수치", required = true,example = "145")
    private Integer maxBloodPressure;

    @ApiModelProperty(value = "최소 혈압 수치", required = true,example = "145")
    private Integer minBloodPressure;

    @ApiModelProperty(value = "맥박 수치", required = true,example = "145")
    private Integer heartBeat;

    @ApiModelProperty(value = "Record ID",example = "123123")
    private Long recordId;


}
