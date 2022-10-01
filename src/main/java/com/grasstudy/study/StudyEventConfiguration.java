package com.grasstudy.study;

import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
public class StudyEventConfiguration {

	@Bean
	public Supplier<Flux<StudyCreateEvent>> studyCreateEventPublisher(StudyEventPublisher studyEventPublisher) {
		return () -> studyEventPublisher.createEventFlux();
	}

}
