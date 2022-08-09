package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel(value = "혈당 기록 수정 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BSugarUpdateVO {

    @ApiModelProperty(value = "시간 태그",example = "아침", required = true)
    @NotNull(message = "Time tag cannot be null")
    private String timeTag;

    @ApiModelProperty(value = "혈당 수치",example = "145", required = true)
    @NotNull(message = "Record content cannot be null")
    private Integer bloodSugar;

    @ApiModelProperty(value = "Record ID",example = "123123", required = true)
    @NotNull(message = "Record Id cannot be null")
    private Long recordId;


}
