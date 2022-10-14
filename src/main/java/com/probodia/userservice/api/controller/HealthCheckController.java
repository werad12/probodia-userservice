package com.probodia.userservice.api.controller;

import com.probodia.userservice.config.properties.AppProperties;
import com.probodia.userservice.config.rabbitmq.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@RequiredArgsConstructor
public class HealthCheckController {

    @Value("${server.port}")
    String serverPort;


    private final RabbitMqConfig rabbitMqConfig;

    private final RabbitTemplate rabbitTemplate;

    private final AppProperties appProperties;


    @GetMapping("health_check")
    public String healthCheck(){

        return "Server is working on port : "+ serverPort +
                ",Server access token time : "+ appProperties.getAuth().getTokenExpiry();

    }

    @GetMapping("testrabbit")
    public String testRabbit(){

        rabbitTemplate.convertAndSend(rabbitMqConfig.getExchageName(), rabbitMqConfig.getRoutingKey(),
                "hello world");

        return "OK";
    }




    @GetMapping("header_test")
    public String headerTest(@RequestHeader(value = "Authorization")String token){
        return token;
    }


}
