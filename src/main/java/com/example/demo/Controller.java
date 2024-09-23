package com.example.demo;

import brave.baggage.BaggageField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);


    @GetMapping("/v1/demo")
    public ResponseEntity<String> demo() {
        String baggageFiledValue = BaggageField.getByName("user-uuid").getValue();
        LOGGER.info("Propagated in the header: {}", baggageFiledValue);
        return new ResponseEntity<>(baggageFiledValue, HttpStatus.OK);
    }
}
