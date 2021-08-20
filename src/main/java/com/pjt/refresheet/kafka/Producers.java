package com.pjt.refresheet.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@RequiredArgsConstructor
public class Producers {

    private final Logger logger  = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String payload){
        logger.info("sending payload = '{} to topic-{}'", payload, topic);
        ListenableFuture<SendResult<String, String>> listenable =
                kafkaTemplate.send(topic, payload);
    }
}
