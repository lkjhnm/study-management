package com.grasstudy;

import com.grasstudy.study.event.AttendEventPublisher;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.AttendEvent;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
public class OutboundEventConfiguration {

	@Bean
	public Supplier<Flux<StudyCreateEvent>> studyCreateEventPublisher(StudyEventPublisher studyEventPublisher) {
		return () -> studyEventPublisher.createEventFlux();
	}

	@Bean
	public Supplier<Flux<AttendEvent>> attendCreateEventPublisher(AttendEventPublisher attendEventPublisher) {
		return () -> attendEventPublisher.attendCreateEventFlux();
	}

}
