package com.probodia.userservice.api.vo.recordstat;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel(value = "일일 평균 영양소 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AverageNeutrientVO {

    private Double protein;
    private Double carbohydrate;
    private Double fat;

}
