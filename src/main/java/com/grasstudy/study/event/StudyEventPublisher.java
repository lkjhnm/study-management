package com.grasstudy.study.event;

import com.grasstudy.study.event.scheme.StudyCreateEvent;
import com.grasstudy.study.event.scheme.StudyJoinEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Service
public class StudyEventPublisher {

	private final Flux<StudyCreateEvent> createEventFlux;
	private final Flux<StudyJoinEvent> joinEventFlux;

	private Consumer<StudyCreateEvent> createEventConsumer;
	private Consumer<StudyJoinEvent> joinEventConsumer;

	public StudyEventPublisher() {
		createEventFlux = Flux.create(fluxSink -> createEventConsumer = fluxSink::next);
		joinEventFlux = Flux.create(fluxSink -> joinEventConsumer = fluxSink::next);
	}

	public void publish(EventMessage eventMessage) {
		if (eventMessage instanceof StudyCreateEvent) {
			this.createEventConsumer.accept((StudyCreateEvent) eventMessage);
		} else if (eventMessage instanceof StudyJoinEvent) {
			this.joinEventConsumer.accept((StudyJoinEvent) eventMessage);
		}
	}

	public Flux<StudyCreateEvent> createEventFlux() {
		return this.createEventFlux;
	}

	public Flux<StudyJoinEvent> joinEventFlux() {
		return this.joinEventFlux;
	}
}
