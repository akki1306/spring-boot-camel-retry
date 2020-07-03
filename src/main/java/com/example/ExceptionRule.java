package com.example;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component
public class ExceptionRule {
    public boolean shouldRetry(@Header(Exchange.REDELIVERY_COUNTER) Integer counter, Exception causedBy) {
        if (causedBy.getMessage().equals("122") && counter <=3) {
            return true;
        } else {
            return false;
        }
    }
}
