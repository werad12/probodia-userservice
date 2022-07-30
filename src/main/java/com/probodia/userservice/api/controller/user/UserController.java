package com.probodia.userservice.api.controller.user;

import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.service.UserService;
import com.probodia.userservice.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;





}
