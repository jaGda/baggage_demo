package com.example.demo;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation.FactoryBuilder;
import brave.baggage.BaggagePropagationConfig.SingleBaggageField;
import brave.baggage.BaggagePropagationCustomizer;
import org.springframework.stereotype.Component;

@Component
public class UserUuidContextPropagationCustomizer implements BaggagePropagationCustomizer {

    public UserUuidContextPropagationCustomizer() {
    }

    @Override
    public void customize(FactoryBuilder builder) {
        builder.add(SingleBaggageField.remote(BaggageField.create("user-uuid")));
    }
}
