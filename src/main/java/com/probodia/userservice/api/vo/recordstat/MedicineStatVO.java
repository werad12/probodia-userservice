package com.probodia.userservice.api.vo.recordstat;

import com.probodia.userservice.api.vo.medicine.MedicineStatisticDetailVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;
import java.util.Map;

@ApiModel(value = "투약 기록 분석 응답")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineStatVO {
    List<MedicineStatisticDetailVO> medicineList;
}
