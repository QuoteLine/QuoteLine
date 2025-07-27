package com.quoteline.quote_server.subscription.controller;

import com.quoteline.quote_server.subscription.domain.Subscription;
import com.quoteline.quote_server.subscription.dto.SubscriptionRequest;
import com.quoteline.quote_server.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribes")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Subscription> subscribe(@RequestBody SubscriptionRequest request) {
        Subscription subscription = subscriptionService.createSubscription(request);
        return ResponseEntity.ok(subscription);
    }

    @DeleteMapping
    public ResponseEntity<Void> unsubscribe(@RequestParam("email") String email) {
        subscriptionService.deleteSubscription(email);
        return ResponseEntity.noContent().build();
    }
}