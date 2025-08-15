package com.quoteline.quote_server.quote.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class QuoteService {
    private final RedisTemplate<String, String> redisTemplate;
    private final Counter quoteRequestCounter;
    private static final String CACHE_KEY = "DAILY_QUOTE";
    private static final List<String> DUMMY_QUOTES = List.of(
            "그냥 해. 변명이 왜 이렇게 많아",
            "할거면 제대로하고, 하기 싫으면 그만해"
    );
    private final Random random = new Random();

    public QuoteService(RedisTemplate<String, String> redisTemplate, MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.quoteRequestCounter = Counter.builder("quote.requests")
                .description("Number of quote requests")
                .register(meterRegistry);
    }

    public String getQuote() {
        quoteRequestCounter.increment();
        String cached = redisTemplate.opsForValue().get(CACHE_KEY);
        if(cached != null) {
            return cached;
        }
        String quote = DUMMY_QUOTES.get(random.nextInt(DUMMY_QUOTES.size()));
        redisTemplate.opsForValue().set(CACHE_KEY, quote, 1, TimeUnit.DAYS);
        return quote;
    }
}
