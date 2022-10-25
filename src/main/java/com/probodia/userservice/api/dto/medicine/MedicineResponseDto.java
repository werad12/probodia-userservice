package com.probodia.userservice.api.dto.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@ApiModel(value = "투약 기록 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineResponseDto {

    @ApiModelProperty(value = "시간 태그",example = "아침")
    private String timeTag;


    @ApiModelProperty(value = "Record ID",example = "123123")
    private Long recordId;

    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;

    @ApiModelProperty(value = "투약 기록 정보", required = true)
    private List<MedicineDetailResponseDto> medicineDetails;


}
