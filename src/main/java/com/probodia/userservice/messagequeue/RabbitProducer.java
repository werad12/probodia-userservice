package com.probodia.userservice.messagequeue;

import com.probodia.userservice.api.dto.bsugar.BSugarResponse;
import com.probodia.userservice.api.dto.meal.MealResponseDto;
import com.probodia.userservice.config.rabbitmq.RabbitProducerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitProducer {

    private final RabbitProducerConfig rabbitMqConfig;
    private final RabbitTemplate rabbitTemplate;

    public void sendFood(MealResponseDto mealResponseDto){

        rabbitTemplate.convertAndSend(rabbitMqConfig.getExchageName(), rabbitMqConfig.getRoutingKey(),
                mealResponseDto);
    }

    public void sendSugar(BSugarResponse bSugarResponse){

        rabbitTemplate.convertAndSend(rabbitMqConfig.getExchageName(), rabbitMqConfig.getRoutingKey(),
                bSugarResponse);
    }


}
