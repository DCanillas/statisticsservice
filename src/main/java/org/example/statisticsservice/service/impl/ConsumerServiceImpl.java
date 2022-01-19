package org.example.statisticsservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.modelproject.dto.MessageKafkaDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerServiceImpl {

    @KafkaListener(topics = "orders", groupId= "orderGroup")
    public void consumeFromTopic(MessageKafkaDTO message){
        log.info(String.valueOf(message));
    }

}
