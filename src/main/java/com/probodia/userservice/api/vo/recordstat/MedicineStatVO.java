package com.probodia.userservice.api.vo.recordstat;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Map;

@ApiModel(value = "투약 기록 분석 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineStatVO {
    Map<String, Integer> recordMedicineList;
}
