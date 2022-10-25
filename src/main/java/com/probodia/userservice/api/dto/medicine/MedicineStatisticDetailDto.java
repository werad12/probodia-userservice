package com.probodia.userservice.api.dto.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "투약 상세 분석 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineStatisticDetailDto {

    @ApiModelProperty(value = "약 이름",example = "센트롬")
    private String medicineName;
    @ApiModelProperty(value = "투약 개수",example = "34")
    private Integer medicineCnt;

}
