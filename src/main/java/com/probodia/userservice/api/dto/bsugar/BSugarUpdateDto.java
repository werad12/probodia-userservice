package com.probodia.userservice.api.dto.bsugar;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@ApiModel(value = "혈당 기록 수정 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BSugarUpdateDto {

    @ApiModelProperty(value = "시간 태그",example = "아침", required = true)
    @NotNull(message = "Time tag cannot be null")
    private String timeTag;

    @ApiModelProperty(value = "혈당 수치",example = "145", required = true)
    @NotNull(message = "Record content cannot be null")
    private Integer bloodSugar;

    @ApiModelProperty(value = "Record ID",example = "123123", required = true)
    @NotNull(message = "Record Id cannot be null")
    private Long recordId;
    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @NotNull(message = "Record time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;

}
