package com.probodia.userservice.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.java.Log;

import javax.validation.constraints.NotNull;

@ApiModel(value = "기록 삭제 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordDeleteRequest {
    @ApiModelProperty(value = "유저 ID", required = true,example = "123123")
    @NotNull(message = "User Id cannot be null")
    Long userId;
    @ApiModelProperty(value = "유저 ID", required = true,example = "123123")
    @NotNull(message = "Record Id cannot be null")
    Long recordId;

}
