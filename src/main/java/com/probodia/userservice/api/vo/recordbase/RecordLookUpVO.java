package com.probodia.userservice.api.vo.recordbase;

import com.probodia.userservice.api.entity.record.Records;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel(value = "기록 조회 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordLookUpVO {
    @ApiModelProperty(value = "기록의 타입을 나타낸다.", required = true,example = "MEAL")
    private String type;

    @ApiModelProperty(value = "기록의 정보가 들어있다.", required = true,example = "type에 따른 기록이 나온다.")
    private Object record;

}
