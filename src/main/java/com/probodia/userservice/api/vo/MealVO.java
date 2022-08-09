package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "음식 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealVO {

    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    @NotNull(message = "Time tag cannot be null")
    @Size(max = 10, message="Time tag not be more than 10 charaters")
    private String timeTag;

    @ApiModelProperty(value = "종류에 따른 음식의 정보", required = true)
    @NotNull(message = "Meal Detail cannot be null")
    private List<MealDetailVO> mealDetails;

}
