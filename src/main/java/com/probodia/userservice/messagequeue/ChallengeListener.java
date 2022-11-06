package com.probodia.userservice.messagequeue;

import com.probodia.userservice.messagequeue.entity.PointVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "${messageQueue.point-queue}")
public class ChallengeListener {

    @RabbitHandler
    public void listenerFood(PointVO request){
        log.debug("Req : {} {}",request.getUserId(), request.getPoint());

    }

}
