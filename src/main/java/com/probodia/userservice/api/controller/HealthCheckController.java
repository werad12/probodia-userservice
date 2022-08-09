package com.probodia.userservice.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    Environment env;

    @Autowired
    public HealthCheckController(Environment env) {
        this.env = env;
    }

    @GetMapping("health_check")
    public String healthCheck(){

        return "Server is working on port : "+ env.getProperty("server.port");

    }

    @GetMapping("header_test")
    public String headerTest(@RequestHeader(value = "Authorization")String token){
        return token;
    }


}
