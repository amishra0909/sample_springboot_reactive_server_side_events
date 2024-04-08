package com.abhishek.launchdarkly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.reactive.function.client.WebClient;

import com.abhishek.launchdarkly.service.Validator;
import com.abhishek.launchdarkly.service.impl.InputValidator;

@Configuration
public class ApplicationConfig {

    @Bean
    @Description("Server sent events reactive client")
    WebClient sseClient(@Value("${testscore.service.url}") String serviceUrl) {

        return WebClient.builder()
            .baseUrl(serviceUrl)
            .build();
    }

    @Bean
    @Description("Service to validate input")
    Validator inputValidator() {
        return new InputValidator();
    }

}
