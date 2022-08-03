package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "투약 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineVO {

    @ApiModelProperty(value = "유저 ID", required = true,example = "123123")
    @NotNull(message = "User Id cannot be null")
    private String userId;
    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    @NotNull(message = "Time tag cannot be null")
    @Size(max = 10, message="Time tag not be more than 10 characters")
    private String timeTag;

    @ApiModelProperty(value = "하루 투약 횟수", required = true,example = "145")
    @NotNull(message = "Record content cannot be null")
    private Integer medicineCnt;

    @ApiModelProperty(value = "약 이름",required = true,example = "센트롬")
    @Size(max = 50, message = "Medicine name not be more than 50 characters")
    @NotNull(message = "Medicine name cannot be null")
    private String medicineName;

    @ApiModelProperty(value = "약 ID",example = "FP1234")
    private String medicineId;



}
