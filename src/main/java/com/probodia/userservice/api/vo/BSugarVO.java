package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "혈당 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BSugarVO {

    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    @NotNull(message = "Time tag cannot be null")
    @Size(max = 10, message="Time tag not be more than 10 charaters")
    private String timeTag;

    @ApiModelProperty(value = "혈당 수치", required = true,example = "145")
    @NotNull(message = "Record content cannot be null")
    private Integer bloodSugar;

}
