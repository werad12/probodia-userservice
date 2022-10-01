package com.probodia.userservice.api.vo.recordstat;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel(value = "범위 분석 혈당 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RangeBSugarVO {
    private Integer total;
    private Integer low;
    private Integer mid;
    private Integer high;
}
