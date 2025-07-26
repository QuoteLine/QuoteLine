package com.quoteline.quote_server.subscription.repository;

import com.quoteline.quote_server.subscription.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}