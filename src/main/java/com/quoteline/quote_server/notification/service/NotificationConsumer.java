package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final Optional<JavaMailSender> mailSender;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @KafkaListener(topics = "#{'${app.kafka.topic.email:email-topic}'}", groupId = "quote-mail")
    public void consume(EmailPayload payload) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("quoteline.com");
        msg.setTo(payload.getEmail());
        msg.setSubject("오늘의 명언");
        msg.setText(payload.getQuote());
        mailSender.get().send(msg);
        log.info("Send email to {} with quote: {}", payload.getEmail(), payload.getQuote());
    }
}
