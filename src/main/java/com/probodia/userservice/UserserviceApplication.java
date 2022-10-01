package com.probodia.userservice;

import com.probodia.userservice.config.properties.AppProperties;
import com.probodia.userservice.config.properties.CorsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties({
        CorsProperties.class,
        AppProperties.class
})
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
public class UserserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);
    }
    @PostConstruct
    void init() {
        //TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
    @Bean
    public RestTemplate httpRestTemplate(){
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

}
