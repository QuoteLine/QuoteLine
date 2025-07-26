package com.quoteline.quote_server.subscription.service;

import com.quoteline.quote_server.subscription.domain.Subscription;
import com.quoteline.quote_server.subscription.dto.SubscriptionRequest;
import com.quoteline.quote_server.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription createSubscription(SubscriptionRequest request) {
        Subscription subscription = Subscription.builder()
                .age(request.getAge())
                .gender(request.getGender())
                .email(request.getEmail())
                .timeSegment(request.getTimeSegment())
                .build();
        return subscriptionRepository.save(subscription);
    }
}
