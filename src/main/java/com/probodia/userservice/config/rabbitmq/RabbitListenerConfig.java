package com.probodia.userservice.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.probodia.userservice.messagequeue.entity.PointVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitListenerConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Jackson2JsonMessageConverter getConverter() {
        Jackson2JsonMessageConverter messageConverter =
                new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(getClassMapper());
        messageConverter.setJavaTypeMapper(typeMapper());
//
//        Jackson2JavaTypeMapper javaTypeMapper = messageConverter.getJavaTypeMapper();
//        AbstractJavaTypeMapper javaTypeMapper1 = (AbstractJavaTypeMapper) javaTypeMapper;
//        Map<String, Class<?>> idClassMapping = javaTypeMapper1.getIdClassMapping();
//        idClassMapping.forEach((k,v) -> log.debug("k : {}, v {}",k,v));


        return messageConverter;
    }

    @Bean
    public DefaultJackson2JavaTypeMapper typeMapper(){
        DefaultJackson2JavaTypeMapper classMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.probodia.challengeservice.messagequeue.entity.PointVO",
                PointVO.class);
//        idClassMapping.put("thing2", Thing2.class);
        classMapper.setIdClassMapping(idClassMapping);
        log.debug("CLASSMAPPER : {}", classMapper);
        return classMapper;
    }

    @Bean
    public DefaultClassMapper getClassMapper() {

        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> map = new HashMap<>();
//        map.put("com.probodia.userservice.api.vo.bsugar.BSugarResponse",
//                BSugarResponse.class);
//        map.put("com.probodia.userservice.api.vo.meal.MealResponseVO",
//                MealResponseVO.class);
//        map.put("com.probodia.userservice.api.vo.meal.MealDetailResponseVO",
//                MealDetailResponseVO.class);

        map.put("com.probodia.challengeservice.messagequeue.entity.PointVO",
                PointVO.class);


        classMapper.setIdClassMapping(map);
        classMapper.setTrustedPackages("*");
        return classMapper;
    }

}