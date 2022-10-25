package com.probodia.userservice.api.dto.recordstat;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestFoodDetailDto {
    List<String> foodIds;
}
