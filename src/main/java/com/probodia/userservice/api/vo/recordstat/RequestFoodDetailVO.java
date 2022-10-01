package com.probodia.userservice.api.vo.recordstat;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestFoodDetailVO {
    List<String> foodIds;
}
