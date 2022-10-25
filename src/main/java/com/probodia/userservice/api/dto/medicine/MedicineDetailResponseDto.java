package com.probodia.userservice.api.dto.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "투약 상세 기록 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineDetailResponseDto {
    @ApiModelProperty(value = "투약 상세 ID", required = true,example = "123")
    private Long medicineDetailId;

    @ApiModelProperty(value = "하루 투약 횟수", example = "145")
    private Integer medicineCnt;

    @ApiModelProperty(value = "약 이름",example = "센트롬")
    private String medicineName;

    @ApiModelProperty(value = "약 ID",example = "FP1234")
    private String medicineId;

}
