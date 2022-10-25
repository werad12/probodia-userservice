package com.probodia.userservice.api.dto.recordstat;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel(value = "평균 기록 비율")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordPercentDto {

    private Integer aroundMorning;
    private Integer aroundNoon;
    private Integer aroundNight;

}
