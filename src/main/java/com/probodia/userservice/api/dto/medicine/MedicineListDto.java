package com.probodia.userservice.api.dto.medicine;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@ApiModel(value = "투약 기록 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineListDto {



    private List<MedicineDto> medicineDetails;
    
}
