package com.quoteline.quote_server.notification.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class EmailPayload {
    private final String idempotencyKey;
    private final String email;
    private final String quote;

    @JsonCreator
    public EmailPayload(@JsonProperty("idempotencyKey") String idempotencyKey, @JsonProperty("email") String email, @JsonProperty("quote") String quote) {
        this.idempotencyKey = idempotencyKey;
        this.email = email;
        this.quote = quote;
    }
}