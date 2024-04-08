package com.abhishek.launchdarkly.service;

import com.abhishek.launchdarkly.config.TestApplicationConfig;
import com.abhishek.launchdarkly.service.impl.TestScoreSSEConsumingService;
import com.abhishek.launchdarkly.representation.ScoreRepresentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(classes = {TestApplicationConfig.class, TestScoreSSEConsumingService.class})
@RequiredArgsConstructor
public class ServerSentEventsConsumingServiceTest {

    private final ParameterizedTypeReference<ServerSentEvent<ScoreRepresentation>> type = new ParameterizedTypeReference<>() {};

    @Autowired
    private ServerSentEventsConsumingService sseService;

    @Autowired
    private WebClient sseClient;

    @Test
    @DisplayName("Consuming Server Sent Events")
    public void shouldConsumeServerSentEvents(TestInfo testInfo) {
        log.info("Running test: {}", testInfo.getDisplayName());

        ScoreRepresentation sr1 = new ScoreRepresentation();
        sr1.setExam(1L);
        sr1.setStudentId("student1");
        sr1.setScore(0.991F);
        
        ScoreRepresentation sr2 = new ScoreRepresentation();
        sr2.setExam(2L);
        sr2.setStudentId("student2");
        sr2.setScore(0.999F);

        ServerSentEvent<ScoreRepresentation> sse1 = 
            ServerSentEvent.<ScoreRepresentation>builder()
            .data(sr1)
            .id("id1")
            .build();

        ServerSentEvent<ScoreRepresentation> sse2 = 
            ServerSentEvent.<ScoreRepresentation>builder()
            .data(sr2)
            .id("id2")
            .build();

        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.when(sseClient.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri("/")).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToFlux(type)).thenReturn(Flux.just(sse1, sse2));

        StepVerifier.create(sseService.consume())
            .expectNextMatches(data -> validateProperties("id1", 1L, "student1", 0.991F, data))
            .expectNextMatches(data -> validateProperties("id2", 2L, "student2", 0.999F, data))
            .thenCancel()
            .verify();
    }

    private boolean validateProperties(String id, long examId, String studentId, float score, ServerSentEvent<ScoreRepresentation> rep) {
    
        log.info("representation = {}", rep);
        return rep != null 
            && rep.data() != null 
            && rep.id().equals(id) 
            && rep.data().getExam() == examId 
            && rep.data().getStudentId() == studentId 
            && rep.data().getScore() == score;
    }
}
