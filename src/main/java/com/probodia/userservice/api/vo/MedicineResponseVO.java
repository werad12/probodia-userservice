package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "투약 기록 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineResponseVO {

    @ApiModelProperty(value = "유저 ID", example = "123123")
    private String userId;

    @ApiModelProperty(value = "시간 태그",example = "아침")
    private String timeTag;

    @ApiModelProperty(value = "하루 투약 횟수", example = "145")
    private Integer medicineCnt;

    @ApiModelProperty(value = "약 이름",example = "센트롬")
    private String medicineName;

    @ApiModelProperty(value = "약 ID",example = "FP1234")
    private String medicineId;

    @ApiModelProperty(value = "Record ID",example = "123123")
    private Long recordId;

}
