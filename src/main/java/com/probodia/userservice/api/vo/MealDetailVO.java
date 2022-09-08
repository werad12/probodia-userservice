package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "음식 상세 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealDetailVO {

    @ApiModelProperty(value = "칼로리", required = true,example = "123")
    private Integer calories;

    @ApiModelProperty(value = "음식 이미지", required = true,example = "http://asadfasdf.com")
    private String imageUrl;

    @ApiModelProperty(value = "혈당", required = true,example = "123")
    private Integer bloodSugar;

    @ApiModelProperty(value = "음식 이름", required = true,example = "라면")
    @NotNull(message = "Food name cannot be null")
    private String foodName;

    @ApiModelProperty(value = "음식 수량(그램)", required = true,example = "123")
    @NotNull(message = "Food quantity cannot be null")
    private Integer quantity;


    @ApiModelProperty(value = "음식 ID", required = true,example = "123-abc")
    private String foodId;
    
}
