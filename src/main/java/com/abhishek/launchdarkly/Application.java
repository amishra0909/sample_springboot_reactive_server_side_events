package com.abhishek.launchdarkly;

import com.abhishek.launchdarkly.representation.ScoreRepresentation;
import com.abhishek.launchdarkly.service.DbService;
import com.abhishek.launchdarkly.service.ServerSentEventsConsumingService;

import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import com.abhishek.launchdarkly.service.Validator;

@Slf4j
@EnableMongoRepositories
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    CommandLineRunner start(ServerSentEventsConsumingService service, DbService dbService, Validator validator) {
    
        return args -> {
        
            Flux<ServerSentEvent<ScoreRepresentation>> eventStream = service.consume();

            eventStream.subscribe(data -> {
                ScoreRepresentation representation = data.data();
                if (representation != null) {
                    log.info("Validating the data");
                    if (!validator.validate(representation)) {
                         
                    } else {
                       log.info("Saving to database..");
                       dbService.save(representation);
                       log.info("Saved: {}", representation);
                    }
                }
            });
        };
    }
}
