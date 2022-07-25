package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel(value = "음식 상세 수정 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealDetailUpdateVO {

    @ApiModelProperty(value = "음식 상세 ID", required = true,example = "123123")
    private Long mealDetailId;

    @ApiModelProperty(value = "칼로리", required = true,example = "123")
    private Integer calories;

    @ApiModelProperty(value = "음식 이미지", required = true,example = "http://asadfasdf.com")
    private String imageUrl;

    @ApiModelProperty(value = "혈당", required = true,example = "123")
    private Integer bloodSugar;

    @ApiModelProperty(value = "음식 이름", required = true,example = "라면")
    @NotNull(message = "Food name cannot be null")
    private String foodName;

    @ApiModelProperty(value = "음식 ID", required = true,example = "123")
    private Long foodId;

}
