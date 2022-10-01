package com.grasstudy.study.event;

import com.grasstudy.study.event.scheme.StudyCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Service
public class StudyEventPublisher {

	private final Flux<StudyCreateEvent> createEventFlux;

	private Consumer<StudyCreateEvent> createEventConsumer;

	public StudyEventPublisher() {
		createEventFlux = Flux.create(fluxSink -> createEventConsumer = studyCreateEvent ->  fluxSink.next(studyCreateEvent));
	}

	public void publish(StudyCreateEvent studyCreateEvent) {
		this.createEventConsumer.accept(studyCreateEvent);
	}

	public Flux<StudyCreateEvent> createEventFlux() {
		return this.createEventFlux;
	}
}
