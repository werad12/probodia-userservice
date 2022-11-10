package com.probodia.userservice.api.controller.auth;

import com.probodia.userservice.api.entity.auth.AuthReqModel;
import com.probodia.userservice.api.entity.base.AppVersion;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.entity.user.UserRefreshToken;
import com.probodia.userservice.api.exception.UnAuthorizedException;
import com.probodia.userservice.api.repository.base.VersionRepository;
import com.probodia.userservice.api.repository.user.UserRefreshTokenRepository;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.dto.user.LoginResponseDto;
import com.probodia.userservice.config.properties.AppProperties;
import com.probodia.userservice.oauth.entity.RoleType;
import com.probodia.userservice.oauth.token.AuthToken;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import com.probodia.userservice.utils.CookieUtil;
import com.probodia.userservice.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(value = "AuthController", description = "토큰과 관련된 API")
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserService userService;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";
    private final VersionRepository versionRepository;
    @Value("${version.token}")
    String masterToken;



    @GetMapping("/isCreated")
    @ApiOperation(value = "로그인 되었는지 확인")
    public ResponseEntity<Boolean> isCreated(@RequestHeader(value = "userId") @ApiParam(value = "유저 ID", required = true,example = "123123")
                                                 @NotNull(message = "User Id cannot be null")Long userId){

        String uId = getUser(String.valueOf(userId));
        if(uId==null) return ResponseEntity.status(HttpStatus.OK).body(false);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/api/version/{userVersion}")
    @ApiOperation(value = "앱 버전 확인")
    public ResponseEntity<Boolean> getVersion(@PathVariable(name = "userVersion") @ApiParam(value = "userVersion", required = true,example = "12")
            @NotNull(message = "User Version cannot be null")Integer userVersion){

        Optional<AppVersion> version = versionRepository.findById(Long.valueOf(1));
        if(version.isEmpty()) throw new IllegalArgumentException("No Version. Please try again");

        Boolean canPass = version.get().getAppVersion()<= userVersion ? true : false;

        return ResponseEntity.status(HttpStatus.OK).body(canPass);

    }

    @PostMapping("/version/{updateVersion}")
    @ApiOperation(value = "앱 버전 변경{개발용}")
    public ResponseEntity<Integer> updateVersion(@RequestHeader(value = "Authorization") @ApiParam(value = "마스터 토큰", required = true,example = "123123")
                                                     @NotNull(message = "Token cannot be null")String token,
                                                 @PathVariable(name = "updateVersion") @ApiParam(value = "updateVersion", required = true,example = "12")
                                                 @NotNull(message = "Update Version cannot be null")Integer updateVersion){

        log.info("Token : {}",token);
        log.info("MAster : {}",masterToken);

        if(!masterToken.equals(token)) throw new IllegalArgumentException("Retry with master token");

        Optional<AppVersion> version = versionRepository.findById(Long.valueOf(1));

        version.get().setAppVersion(updateVersion);
        versionRepository.saveAndFlush(version.get());

        return ResponseEntity.status(HttpStatus.OK).body(version.get().getAppVersion());
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 / 회원가입", notes = "로그인 또는 회원가입")
    public ResponseEntity<LoginResponseDto> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
    ) {

        //kakao access token을 가지고 authentication을 진행한다.
        //200 응답 + id 값이 같은지

        Map<String, String> authenticate = userService.authenticate(authReqModel);
        if(Integer.parseInt(authenticate.get("status"))!=(HttpStatus.OK.value()))
            throw new UnAuthorizedException("Invalid status from kakao auth server.");

        String userId = authenticate.get("id");

        if(!userId.equals(authReqModel.getId()))
            throw new UnAuthorizedException("Invalid User Id");

        Boolean isSignup = false;

        //userId로 찾아서 user가 존재하지 않으면 user를 생성한다.
        User user = userService.getUser(userId);
        if(user==null){
            //이 경우에는 회원가입으로 간주.
            Map<String,String> userInfo = userService.getUserInfo(authReqModel);
            isSignup = true;
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

        String token = accessToken.getToken();
        String returnRefreshToken = refreshToken.getToken();
        LoginResponseDto ret = new LoginResponseDto();
        ret.setApiAccessToken(token);
        ret.setApiRefreshToken(returnRefreshToken);
        ret.setIsSignUp(isSignup);


        return new ResponseEntity<>(ret,HttpStatus.OK);
    }



    @GetMapping("/auth/refresh")
    @ApiOperation(value = "server access token 재발급", notes = "server refresh token을 통해 access token을 재발급 받는다." +
            " 리프레시 토큰은 여기서 RefreshToken의 이름으로 헤더에 담는다.")
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
        String refreshToken = HeaderUtil.getHeaderRefreshToken(request);

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
                RoleType.USER.getCode(),
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

    private String getUser(String userId) {
        User user = userService.getUser(userId);

        if(user==null){
            return null;
        }
        return user.getUserId();
    }
}
