package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class NotificationProducerTest {
    @Mock
    private KafkaTemplate<String, EmailPayload> kafkaTemplate;

    @InjectMocks
    private NotificationProducer producer;

    @Test
    void sendUserIdempotencyKeyAsMessageKey() {
        EmailPayload payload = new EmailPayload("id-1", "1stevering@naver.com", "quote");
        ReflectionTestUtils.setField(producer, "topic", "email-topic");
        producer.send(payload);
        verify(kafkaTemplate).send("email-topic", "id-1", payload);
    }

    /* NotificationProducer 단위 테스트 */
    @Test
    void send_sendsToConfiguredTopicWithEmailAsKey() {
        // given
        String topic = "email-topic";
        NotificationProducer producer = new NotificationProducer(kafkaTemplate);
        // @Value 주입 필드 값 세팅
        ReflectionTestUtils.setField(producer, "topic", topic);
        EmailPayload payload = new EmailPayload("wrongKey", "1stevering@naver.com", "hello");
        // when
        producer.send(payload);
        // then
        verify(kafkaTemplate).send(eq(topic), eq("1stevering@naver.com"), eq(payload));
    }
}