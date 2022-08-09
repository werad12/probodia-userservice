package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "혈압 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BPressureVO {

    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    @NotNull(message = "Time tag cannot be null")
    @Size(max = 10, message="Time tag not be more than 10 charaters")
    private String timeTag;

    @ApiModelProperty(value = "최대 혈압 수치", required = true,example = "145")
    @NotNull(message = "Record content cannot be null")
    private Integer maxBloodPressure;
    @ApiModelProperty(value = "최소 혈압 수치", required = true,example = "145")
    @NotNull(message = "Record content cannot be null")
    private Integer minBloodPressure;
    @ApiModelProperty(value = "맥박 수치", required = true,example = "145")
    @NotNull(message = "Record content cannot be null")
    private Integer heartBeat;

}
