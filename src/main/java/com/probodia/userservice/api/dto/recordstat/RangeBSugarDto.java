package com.probodia.userservice.api.dto.recordstat;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel(value = "범위 분석 혈당 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RangeBSugarDto {
    private Integer total;
    private Integer low;
    private Integer mid;
    private Integer high;
}
