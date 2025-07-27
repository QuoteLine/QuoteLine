package com.quoteline.quote_server.quote.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class QuoteService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CACHE_KEY = "DAILY_QUOTE";
    private static final List<String> DUMMY_QUOTES = List.of(
            "그냥 해. 변명이 왜 이렇게 많아",
            "할거면 제대로하고, 하기 싫으면 그만해"
    );
    private final Random random = new Random();

    public String getQuote() {
        String cached = redisTemplate.opsForValue().get(CACHE_KEY);
        if(cached != null) {
            return cached;
        }
        String quote = DUMMY_QUOTES.get(random.nextInt(DUMMY_QUOTES.size()));
        redisTemplate.opsForValue().set(CACHE_KEY, quote, 1, TimeUnit.DAYS);
        return quote;
    }
}
