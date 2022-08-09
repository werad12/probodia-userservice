package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel(value = "혈압 기록 수정 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BPressureUpdateVO {


    @ApiModelProperty(value = "시간 태그",example = "아침", required = true)
    @NotNull(message = "Time tag cannot be null")
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

    @ApiModelProperty(value = "Record ID",example = "123123", required = true)
    @NotNull(message = "Record Id cannot be null")
    private Long recordId;


}
