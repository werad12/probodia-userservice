package com.probodia.userservice.api.dto.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "투약 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineDto {
    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @NotNull(message = "Record time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;

    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    @NotNull(message = "Time tag cannot be null")
    @Size(max = 10, message="Time tag not be more than 10 characters")
    private String timeTag;

    @ApiModelProperty(value = "종류에 따른 음식의 정보", required = true)
    @NotNull(message = "Meal Detail cannot be null")
    private List<MedicineDetailDto> medicineDetails;

}
