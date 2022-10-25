package com.probodia.userservice.api.dto.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "투약 기록 수정 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineUpdateDto {

    @ApiModelProperty(value = "시간 태그",example = "아침", required = true)
    @NotNull(message = "Time tag cannot be null")
    private String timeTag;

    @ApiModelProperty(value = "Record ID",example = "123123", required = true)
    @NotNull(message = "Record Id cannot be null")
    private Long recordId;

    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @NotNull(message = "Record time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;

    @ApiModelProperty(value = "투약 상세 정보", required = true)
    @NotNull(message = "Meal Detail cannot be null")
    private List<MedicineDetailDto> medicineDetails;

}
