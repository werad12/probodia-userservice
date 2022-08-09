package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@ApiModel(value = "음식 기록 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealResponseVO {

    @ApiModelProperty(value = "음식 ID", required = true,example = "123123")
    private Long recordId;

    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    private String timeTag;

    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;

    @ApiModelProperty(value = "종류에 따른 음식의 정보", required = true)
    private List<MealDetailResponseVO> mealDetails;

}
