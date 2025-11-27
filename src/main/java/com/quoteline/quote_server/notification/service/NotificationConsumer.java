package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private static final Duration LOCK_TTL = Duration.ofMinutes(5);
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "#{'${app.kafka.topic.email:email-topic}'}", groupId = "quote-mail")
    public void consume(EmailPayload payload) {
        String lockKey = "email:lock:" + payload.getIdempotencyKey();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, "lock", LOCK_TTL);

        if(Boolean.FALSE.equals(success)) {
            log.info("Duplicated message skipped: {}", payload.getIdempotencyKey());
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("quoteline.com");
        msg.setTo(payload.getEmail());
        msg.setSubject("오늘의 명언");
        msg.setText(payload.getQuote());
        mailSender.send(msg);
        log.info("Send email to {} with quote: {}", payload.getEmail(), payload.getQuote());
    }
}
