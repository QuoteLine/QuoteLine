package com.quoteline.quote_server.notification.service;

import com.quoteline.quote_server.notification.domain.EmailPayload;
import com.quoteline.quote_server.quote.service.QuoteService;
import com.quoteline.quote_server.subscription.domain.Subscription;
import com.quoteline.quote_server.subscription.domain.TimeSegment;
import com.quoteline.quote_server.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyNotificationScheduler {
    private final SubscriptionRepository subscriptionRepository;
    private final QuoteService quoteService;
    private final NotificationProducer producer;

    @Scheduled(cron = "0 0 6 * * *")
    public void sendMorningQuotes() {
        send(TimeSegment.AM);
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void sendEveningQuotes() {
        send(TimeSegment.PM);
    }

    private void send(TimeSegment segment) {
        List<Subscription> subs = subscriptionRepository.findByTimeSegment(segment);
        String quote = quoteService.getQuote();
        subs.forEach(s -> producer.send(new EmailPayload(s.getEmail(), quote)));
    }
}
