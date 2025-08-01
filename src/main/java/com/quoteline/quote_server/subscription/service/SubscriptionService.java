package com.quoteline.quote_server.subscription.service;

import com.quoteline.quote_server.exception.ErrorCode;
import com.quoteline.quote_server.exception.QuoteLineException;
import com.quoteline.quote_server.subscription.domain.Subscription;
import com.quoteline.quote_server.subscription.dto.SubscriptionRequest;
import com.quoteline.quote_server.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription createSubscription(SubscriptionRequest request) {
        if(subscriptionRepository.existsByEmail(request.getEmail())) {
            throw new QuoteLineException(ErrorCode.REPEATED_EMAIL, "");
        }

        Subscription subscription = Subscription.builder()
                .age(request.getAge())
                .gender(request.getGender())
                .email(request.getEmail())
                .timeSegment(request.getTimeSegment())
                .build();
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public void deleteSubscription(String email) {
        subscriptionRepository.deleteByEmail(email);
    }
}
