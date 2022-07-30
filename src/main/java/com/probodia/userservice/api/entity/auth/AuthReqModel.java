package com.probodia.userservice.api.entity.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthReqModel {
    @ApiModelProperty(value = "유저 ID", example = "123123", required = true)
    @NotNull
    private String id;
    @ApiModelProperty(value = "유저 access token", example = "asdfa124", required = true)
    @NotNull
    private String oauthAccessToken;
}
