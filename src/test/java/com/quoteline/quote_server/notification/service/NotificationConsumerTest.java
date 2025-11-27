package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOps;
    private NotificationConsumer consumer;

    @Captor
    ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        consumer = new NotificationConsumer(mailSender, redisTemplate);
    }

    @Test
    void sendEmailWhenLockAcquired() {
        EmailPayload payload = new EmailPayload("id-1", "1stevering@naver.com", "quote");
        when(valueOps.setIfAbsent(anyString(), eq("lock"), any(Duration.class))).thenReturn(true);
        consumer.consume(payload);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void skipWhenDuplicateMessage() {
        EmailPayload payload = new EmailPayload("id-1", "1stevering@naver.com", "quote");
        when(valueOps.setIfAbsent(anyString(), eq("lock"), any(Duration.class))).thenReturn(false);
        consumer.consume(payload);
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    /* NotificationConsumer 단위 테스트 */
    @Test
    void consume_sendsEmailWhenLockAcquired() {
        // given
        NotificationConsumer consumer = new NotificationConsumer(mailSender, redisTemplate);
        EmailPayload payload = new EmailPayload("id-1", "1stevering@naver.com", "quote");
        // 락 획득 성공
        when(redisTemplate.opsForValue().setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(Boolean.TRUE);
        // when
        consumer.consume(payload);
        // then
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage sent = messageCaptor.getValue();
        assertEquals("1stevering@naver.com", sent.getTo()[0]);
        assertEquals("오늘의 명언", sent.getSubject());
        assertEquals("quote", sent.getText());
    }

    @Test
    void consume_skipsWhenDuplicateLockExists() {
        // given
        NotificationConsumer consumer = new NotificationConsumer(mailSender, redisTemplate);
        EmailPayload payload = new EmailPayload("id-1", "1stevering@naver.com", "quote");
        when(redisTemplate.opsForValue().setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(Boolean.FALSE);
        // when
        consumer.consume(payload);
        // then
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}
