package com.probodia.userservice.api.dto.recordstat;

import com.probodia.userservice.api.dto.medicine.MedicineStatisticDetailDto;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@ApiModel(value = "투약 기록 분석 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineStatDto {
    List<MedicineStatisticDetailDto> medicineList;
}
