package com.probodia.userservice.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "로그인 응답 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDto {

    @ApiModelProperty(value = "서버 측 access token", required = true,example = "12sadfasd3123")
    String apiAccessToken;

    @ApiModelProperty(value = "서버 측 refresh token", required = true,example = "1231dsaf23")
    String apiRefreshToken;

    @ApiModelProperty(value = "회원가입인지 아닌지", required = true,example = "true")
    Boolean isSignUp;


}
