package com.abhishek.launchdarkly.config;

import com.abhishek.launchdarkly.repository.ExamRepository;
import com.abhishek.launchdarkly.repository.ScoreRepository;
import com.abhishek.launchdarkly.repository.StudentRepository;
import com.abhishek.launchdarkly.representation.ScoreRepresentation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("test")
@Configuration
@ExtendWith(MockitoExtension.class)
public class TestApplicationConfig {

    @Bean
    @Description("Mongo Template")
    public MongoTemplate mongoTemplate() {
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        return mockMongoTemplate;
    }

    @Bean
    @Description("Exam repository")
    public ExamRepository examRepository() {

        ExamRepository mockExamRepository = Mockito.mock(ExamRepository.class);
        return mockExamRepository;
    }

    @Bean
    @Description("Score repository")
    public ScoreRepository scoreRepository() {

        ScoreRepository mockScoreRepository = Mockito.mock(ScoreRepository.class);
        return mockScoreRepository;
    }

    @Bean
    @Description("Student repository")
    public StudentRepository studentRepository() {

        StudentRepository mockStudentRepository = Mockito.mock(StudentRepository.class);
        return mockStudentRepository;
    }

    @Bean
    @Description("Server sent events reactive client")
    public WebClient sseClient(@Value("${testscore.service.url}") String serviceUrl) {

        WebClient webClientMock = Mockito.mock(WebClient.class);
        return webClientMock;
    }
}

