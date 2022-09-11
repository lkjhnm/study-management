package com.grasstudy.study;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.StudyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class StudyEventConfiguration {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Bean
	public Supplier<Flux<StudyEvent>> studyPublisher() {
		return () -> StudyEventPublisher.studyEventFlux();
	}

	@Bean
	public Consumer<StudyEvent> studyConsumer() {
		return event -> System.out.println(event + " with study consumer");
	}
}
