package com.grasstudy.study.event;

import com.grasstudy.study.event.scheme.AttendEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Service
public class AttendEventPublisher {

	private final Flux<AttendEvent> attendCreateEventFlux;

	private Consumer<AttendEvent> attendCreateEventConsumer;

	public AttendEventPublisher() {
		attendCreateEventFlux = Flux.create(fluxSink -> attendCreateEventConsumer = fluxSink::next);
	}

	public void publish(AttendEvent eventMessage) {
		this.attendCreateEventConsumer.accept(eventMessage);
	}

	public Flux<AttendEvent> attendCreateEventFlux() {
		return this.attendCreateEventFlux;
	}
}
