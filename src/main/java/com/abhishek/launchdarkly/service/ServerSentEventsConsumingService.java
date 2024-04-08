package com.abhishek.launchdarkly.service;

import com.abhishek.launchdarkly.representation.ScoreRepresentation;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface ServerSentEventsConsumingService {

    Flux<ServerSentEvent<ScoreRepresentation>> consume();

}
