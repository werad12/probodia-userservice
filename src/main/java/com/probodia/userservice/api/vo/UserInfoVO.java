package com.probodia.userservice.api.vo;

import com.probodia.userservice.api.entity.enums.base.DiabeteCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel(value = "유저 정보 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoVO {

    @ApiModelProperty(value = "유저 ID", required = true,example = "123123")
    private String userId;

    @ApiModelProperty(value = "키", required = true,example = "123123")
    private Integer height;

    @ApiModelProperty(value = "몸무게", required = true,example = "123123")
    private Integer weight;

    @ApiModelProperty(value = "프로필 이미지", required = true,example = "http://k.kakaocdn.net/dn/hSJFV/btreg2RIL0j/qsB7yCdkQMwJEGtgmrqTzK/img_110x110.jpg")
    private String profileImageUrl;

    @ApiModelProperty(value = "성별", required = true,example = "M")
    private String sex;

    @ApiModelProperty(value = "당뇨 코드", required = true,example = "2형 당뇨")
    private DiabeteCode diabeteCode;

    @ApiModelProperty(value = "나이", required = true,example = "16")
    private Integer age;


}
