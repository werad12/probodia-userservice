package com.probodia.userservice.api.controller.user;

import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.user.UserService;
import com.probodia.userservice.api.vo.UserInfoRequestVO;
import com.probodia.userservice.api.vo.UserInfoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "user Id로 전체 기록을 가져온다.", notes = "모든 기록을 가져온다.")
    public ResponseEntity<UserInfoVO> getUserInfo(@RequestHeader(value = "userId") @ApiParam(value = "유저 ID", required = true,example = "123123")
                                                      @NotNull(message = "User Id cannot be null")Long userId) {
        //user 찾기
        User user = getUser(String.valueOf(userId));

        //user의 정보 가져오기
        UserInfoVO userInfo = userService.getUserInfo(user);
        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation(value = "유저 정보 기록 수정.", notes = "user id, profile, username을 제외한 모든 정보를 수정할 수 있다.")
    public ResponseEntity<UserInfoVO> updateUserInfo(@RequestBody UserInfoRequestVO request) {
        //user 찾기
        User user = getUser(String.valueOf(request.getUserId()));

        //user의 정보 가져오기
        UserInfoVO userInfo = userService.updateUserInfo(request,user);
        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }


    private User getUser(String userId) {
        User user = userService.getUser(userId);

        if(user==null){
            throw new UsernameNotFoundException("Not found User by userId");
        }
        return user;
    }

}
