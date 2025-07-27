package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, EmailPayload> kafkaTemplate;

    @Value("${app.kafka.topic.email:email-topic}")
    private String topic;

    public void send(EmailPayload payload) {
        kafkaTemplate.send(topic, payload.getEmail(), payload);
    }
}
