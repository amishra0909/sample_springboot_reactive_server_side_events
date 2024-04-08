package com.abhishek.launchdarkly.service.impl;

import com.abhishek.launchdarkly.representation.ScoreRepresentation;
import com.abhishek.launchdarkly.service.ServerSentEventsConsumingService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class TestScoreSSEConsumingService implements ServerSentEventsConsumingService {

    private static final String RELATIVE_URL = "/";

    private final WebClient sseClient;

    private final ParameterizedTypeReference<ServerSentEvent<ScoreRepresentation>> type = new ParameterizedTypeReference<>() {};

    public Flux<ServerSentEvent<ScoreRepresentation>> consume() {
        return sseClient.get()
            .uri(TestScoreSSEConsumingService.RELATIVE_URL)
            .retrieve()
            .bodyToFlux(type);
    }
}

