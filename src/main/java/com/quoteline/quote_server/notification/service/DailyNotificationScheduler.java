package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import com.quoteline.quote_server.quote.service.QuoteService;
import com.quoteline.quote_server.subscription.domain.Subscription;
import com.quoteline.quote_server.subscription.domain.TimeSegment;
import com.quoteline.quote_server.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DailyNotificationScheduler {
    private static final Duration LOCK_TTL = Duration.ofMinutes(10);
    private static final String MORNING_LOCK_KEY = "scheduler:morning";
    private static final String EVENING_LOCK_KEY = "scheduler:evening";

    private final SubscriptionRepository subscriptionRepository;
    private final QuoteService quoteService;
    private final NotificationProducer producer;
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void sendMorningQuotes() {
        if(!acquireLock(EVENING_LOCK_KEY)) {
            return;
        }
        send(TimeSegment.AM);
    }

    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    public void sendEveningQuotes() {
        send(TimeSegment.PM);
    }

    private boolean acquireLock(String key) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "lock", LOCK_TTL));
    }

    private void send(TimeSegment segment) {
        List<Subscription> subs = subscriptionRepository.findByTimeSegment(segment);
        String quote = quoteService.getQuote();
        subs.forEach(s -> {
                String key = UUID.randomUUID().toString();
                producer.send(new EmailPayload(key, s.getEmail(), quote));
        });
    }
}