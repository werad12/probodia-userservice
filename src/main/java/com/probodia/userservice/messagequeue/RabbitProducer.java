package com.probodia.userservice.messagequeue;

import com.probodia.userservice.api.vo.bsugar.BSugarResponse;
import com.probodia.userservice.api.vo.meal.MealResponseVO;
import com.probodia.userservice.config.rabbitmq.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitProducer {

    private final RabbitMqConfig rabbitMqConfig;
    private final RabbitTemplate rabbitTemplate;

    public void sendFood(MealResponseVO mealResponseVO){

        rabbitTemplate.convertAndSend(rabbitMqConfig.getExchageName(), rabbitMqConfig.getRoutingKey(),
                mealResponseVO);
    }

    public void sendSugar(BSugarResponse bSugarResponse){

        rabbitTemplate.convertAndSend(rabbitMqConfig.getExchageName(), rabbitMqConfig.getRoutingKey(),
                bSugarResponse);
    }


}
