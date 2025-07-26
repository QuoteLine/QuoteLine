package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {
    @KafkaListener(topics = "#{'${app.kafka.topic.email:email-topic}'}", groupId = "quote-mail")
    public void consume(EmailPayload payload) {
        log.info("Send email to {} with quote: {}", payload.getEmail(), payload.getQuote());
    }
}
