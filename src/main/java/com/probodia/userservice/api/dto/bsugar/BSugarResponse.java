package com.probodia.userservice.api.dto.bsugar;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@ApiModel(value = "혈당 기록 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BSugarResponse implements Serializable {

    static final long serialVersionUID = 112415146L;

    @ApiModelProperty(value = "user ID", example = "123123")
    private String userId;

    @ApiModelProperty(value = "시간 태그",example = "아침")
    private String timeTag;

    @ApiModelProperty(value = "혈당 수치",example = "145")
    private Integer bloodSugar;

    @ApiModelProperty(value = "Record ID",example = "123123")
    private Long recordId;

    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;

}
