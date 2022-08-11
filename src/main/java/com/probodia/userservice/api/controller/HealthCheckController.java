package com.probodia.userservice.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
public class HealthCheckController {

    @Value("${server.port}")
    String serverPort;

    @Value("${testConfig}")
    String testConfig;

    @GetMapping("health_check")
    public String healthCheck(){

        return "Server is working on port : "+ serverPort;

    }

    @GetMapping("config_test")
    public String configTest(){
        return testConfig;
    }

    @GetMapping("header_test")
    public String headerTest(@RequestHeader(value = "Authorization")String token){
        return token;
    }


}
