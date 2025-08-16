package com.quoteline.quote_server.notification.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailPayloadTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void jsonRoundTripMaintainsFields() throws Exception {
        EmailPayload payload = new EmailPayload("key1", "1stevering@naver.com", "quote");
        String json = mapper.writeValueAsString(payload);
        EmailPayload restored = mapper.readValue(json, EmailPayload.class);
        assertEquals("key1", restored.getIdempotencyKey());
        assertEquals("1stevering@naver.com", restored.getEmail());
        assertEquals("quote", restored.getQuote());
    }
}