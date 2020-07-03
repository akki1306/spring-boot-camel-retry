package com.example;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.processor.exceptionpolicy.ExceptionPolicyKey;
import org.apache.camel.processor.exceptionpolicy.ExceptionPolicyStrategy;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaName;
import java.util.Map;
import java.util.Random;

@Component
public class MyRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Throwable.class)
                .retryWhile(bean(ExceptionRule.class))
                .redeliveryDelay(15000)
                .to("amqp:queue:errorQueue")
                .handled(true);

        from("amqp:queue:inputQueue")
                .process(e -> {
                    Integer header = (Integer) e.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
                    if (header == null || header <= 2) {
                        throw new RuntimeException("122");
                    } else {
                        throw new RuntimeException("233");
                    }
                })
                .end()
                .log("Finally")
                .to("amqp:queue:outputQueue");

    }
}
