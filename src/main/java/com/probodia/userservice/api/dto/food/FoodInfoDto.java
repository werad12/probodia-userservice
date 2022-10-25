package com.probodia.userservice.api.dto.food;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

//@ApiModel(value = "기록 삭제 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "음식 상세 정보 응답")
public class FoodInfoDto {
    @ApiModelProperty(value = "큰 카테고리",example = "음식", required = true)
    @NotNull(message = "Big category cannot be null")
    private String bigCategory;

    @ApiModelProperty(value = "작은 카테고리",example = "가공식품", required = true)
    @NotNull(message = "Small category cannot be null")
    private String smallCategory;

    @ApiModelProperty(value = "음식 이름",example = "새우깡", required = true)
    @NotNull(message = "Food name cannot be null")
    private String name;

    @ApiModelProperty(value = "일회 제공량",example = "500", required = true)
    private Integer quantityByOne;

    @ApiModelProperty(value = "단위",example = "g", required = true)
    private String quantityByOneUnit;

    @ApiModelProperty(value = "칼로리",example = "100", required = true)
    private Double calories;
    @ApiModelProperty(value = "탄수화물",example = "100", required = true)
    private Double carbohydrate;
    @ApiModelProperty(value = "혈당",example = "100", required = true)
    private Double sugars;
    @ApiModelProperty(value = "단백질",example = "100", required = true)
    private Double protein;
    @ApiModelProperty(value = "지방",example = "100", required = true)
    private Double fat;
    @ApiModelProperty(value = "트랜스 지방",example = "100", required = true)
    private Double transFat;
    @ApiModelProperty(value = "포화 지방",example = "100", required = true)
    private Double saturatedFat;
    @ApiModelProperty(value = "콜레스테롤",example = "100", required = true)
    private Double cholesterol;
    @ApiModelProperty(value = "나트륨",example = "100", required = true)
    private Double salt;
}
