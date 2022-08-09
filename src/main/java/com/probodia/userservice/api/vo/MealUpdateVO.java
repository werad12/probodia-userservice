package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "음식 수정 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealUpdateVO {

    @ApiModelProperty(value = "음식 ID", required = true,example = "123123")
    @NotNull(message = "Record Id cannot be null")
    private Long recordId;

    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    @NotNull(message = "Time tag cannot be null")
    @Size(max = 10, message="Time tag not be more than 10 charaters")
    private String timeTag;

    @ApiModelProperty(value = "종류에 따른 음식의 정보", required = true)
    @NotNull(message = "Meal Detail cannot be null")
    private List<MealDetailUpdateVO> mealDetails;

}
