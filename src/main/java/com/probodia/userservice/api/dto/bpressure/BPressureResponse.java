package com.probodia.userservice.api.dto.bpressure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@ApiModel(value = "혈압 기록 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BPressureResponse {

    @ApiModelProperty(value = "시간 태그",example = "아침")
    private String timeTag;

    @ApiModelProperty(value = "최대 혈압 수치", required = true,example = "145")
    private Integer maxBloodPressure;

    @ApiModelProperty(value = "최소 혈압 수치", required = true,example = "145")
    private Integer minBloodPressure;

    @ApiModelProperty(value = "맥박 수치", required = true,example = "145")
    private Integer heartBeat;

    @ApiModelProperty(value = "Record ID",example = "123123")
    private Long recordId;

    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;

}
