package com.quoteline.quote_server.subscription.dto;

import com.quoteline.quote_server.subscription.domain.TimeSegment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {
    private Integer age;
    private String gender;
    private String email;
    private TimeSegment timeSegment;
}
