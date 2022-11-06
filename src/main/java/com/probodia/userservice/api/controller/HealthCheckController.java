package com.probodia.userservice.api.controller;

import com.probodia.userservice.config.properties.AppProperties;
import com.probodia.userservice.config.rabbitmq.RabbitProducerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@RequiredArgsConstructor
@Slf4j
public class HealthCheckController {

    @Value("${server.port}")
    String serverPort;


    private final RabbitProducerConfig rabbitMqConfig;

    private final RabbitTemplate rabbitTemplate;

    private final AppProperties appProperties;


    @GetMapping("health_check")
    public String healthCheck(){

        return "Server is working on port : "+ serverPort +
                ",Server access token time : "+ appProperties.getAuth().getTokenExpiry();

    }

    @GetMapping("testSleuth")
    public String testSleuth(){

        log.debug("[testSleuth] : test Info level");
        log.debug("[testSleuth] : test Debug level");

        return "Ok";
    }

    @GetMapping("testrabbit")
    public String testRabbit(){

        rabbitTemplate.convertAndSend(rabbitMqConfig.getExchageName(), rabbitMqConfig.getRoutingKey(),
                "hello world");

        return "OK";
    }




}
