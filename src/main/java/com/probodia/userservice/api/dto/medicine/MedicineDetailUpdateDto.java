package com.probodia.userservice.api.dto.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@ApiModel(value = "투약 기록 상세 수정 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineDetailUpdateDto {

    @ApiModelProperty(value = "투약 상세 ID", required = true,example = "123123")
    //@NotNull(message = "Medicine detail Id cannot be null")
    private Long medicineDetailId;

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
