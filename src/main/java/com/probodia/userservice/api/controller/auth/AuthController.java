package com.probodia.userservice.api.controller.auth;

import com.probodia.userservice.api.entity.auth.AuthReqModel;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.entity.user.UserRefreshToken;
import com.probodia.userservice.api.exception.UnAuthorizedException;
import com.probodia.userservice.api.repository.user.UserRefreshTokenRepository;
import com.probodia.userservice.api.service.UserService;
import com.probodia.userservice.common.ApiResponse;
import com.probodia.userservice.config.properties.AppProperties;
import com.probodia.userservice.oauth.entity.RoleType;
import com.probodia.userservice.oauth.entity.UserPrincipal;
import com.probodia.userservice.oauth.service.CustomOAuth2UserService;
import com.probodia.userservice.oauth.token.AuthToken;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import com.probodia.userservice.utils.CookieUtil;
import com.probodia.userservice.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Map;

import static com.probodia.userservice.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserService userService;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @PostMapping("/login")
    @ApiOperation(value = "로그인 / 회원가입", notes = "로그인 또는 회원가입")
    public ResponseEntity<String> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
    ) {

        log.info("AUTH REQ MODEL : {}",authReqModel);

        //kakao access token을 가지고 authentication을 진행한다.
        //200 응답 + id 값이 같은지

        Map<String, String> authenticate = userService.authenticate(authReqModel);
        if(Integer.parseInt(authenticate.get("status"))!=(HttpStatus.OK.value()))
            throw new UnAuthorizedException("Invalid status from kakao auth server.");

        String userId = authenticate.get("id");

        if(!userId.equals(authReqModel.getId()))
            throw new UnAuthorizedException("Invalid User Id");

//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authReqModel.getId(),
//                        authReqModel.getPassword()
//                )
//        );


        //SecurityContextHolder.getContext().setAuthentication(authentication);

        //userId로 찾아서 user가 존재하지 않으면 user를 생성한다.
        User user = userService.getUser(userId);
        if(user==null){
            Map<String,String> userInfo = userService.getUserInfo(authReqModel);

            user = userService.createUser(userInfo);
        }

        //access 토큰을 생성한다.
        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userId,
                RoleType.USER.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                RoleType.USER.getCode(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
         } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
         }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.deleteCookie(request,response,USER_ID);

        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);
        CookieUtil.addCookie(response,USER_ID,userId,cookieMaxAge);
        //CookieUtil.addCookie(response,USER_TOKEN,accessToken.getToken(),cookieMaxAge);

        String token = accessToken.getToken();

        return new ResponseEntity<>(token,HttpStatus.OK);
    }



    @GetMapping("/api/auth/refresh")
    @ApiOperation(value = "server access token 재발급", notes = "server refresh token을 통해 access token을 재발급 받는다." +
            " 리프레시 토큰은 여기서 refresh_token의 이름으로 쿠키에 담는다.")
    public ResponseEntity<String> refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if (!authToken.validateForExpired()) {
            throw new UnAuthorizedException("Not expired yet.");
        }

        // expired access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new UnAuthorizedException("Invalid status from kakao auth server.");
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (!authRefreshToken.validate()) {
            throw new UnAuthorizedException("Invalid refresh token.");
        }

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            throw new UnAuthorizedException("Invalid refresh token.");
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return new ResponseEntity<>(newAccessToken.getToken(),HttpStatus.OK);
    }
}
