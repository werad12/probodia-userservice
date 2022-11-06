package com.probodia.userservice.api.controller.user;

import com.probodia.userservice.api.annotation.TokenRequired;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.dto.user.UserInfoRequestDto;
import com.probodia.userservice.api.dto.user.UserInfoDto;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthTokenProvider tokenProvider;

    @PostMapping("/point")
    @ApiOperation(value = "챌린지로 포인트 변화시킨다.")
    public String setPoint(@RequestBody Integer point, @TokenRequired User user,@RequestParam("userId") String userId){
        log.debug("POINT {}",point);

        userService.updatePoint(userId, point);

        return "OK";
    }

    @GetMapping
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "모든 기록을 가져온다.")
    public ResponseEntity<UserInfoDto> getUserInfo(@TokenRequired User user,
                                                      @NotNull(message = "User Id cannot be null")Long userId) {


//        log.debug("USER POINT : {}",user.getPoint());

        //user의 정보 가져오기
        UserInfoDto userInfo = userService.getUserInfo(user);
        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation(value = "유저 정보 기록 수정.", notes = "user id, profile, username을 제외한 모든 정보를 수정할 수 있다.")
    public ResponseEntity<UserInfoDto> updateUserInfo(@TokenRequired User user,@RequestBody UserInfoRequestDto request) {
        
        //user의 정보 가져오기
        UserInfoDto userInfo = userService.updateUserInfo(request,user);
        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation(value = "유저 정보 삭제(디버그용).", notes = "회원가입 테스트를 위한 메서드, 추후 삭제 예정")
    public ResponseEntity<String> deleteUser(@TokenRequired User user){
        String userId = userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    
}
