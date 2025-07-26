package com.quoteline.quote_server.subscription.repository;

import com.quoteline.quote_server.subscription.domain.Subscription;
import com.quoteline.quote_server.subscription.domain.TimeSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    List<Subscription> findByTimeSegment(TimeSegment timesegment);
}