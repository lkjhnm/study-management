package com.grasstudy.study.event;

import com.grasstudy.study.event.scheme.AttendEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Service
public class AttendEventPublisher {

	private final Flux<AttendEvent> attendEventFlux;

	private Consumer<AttendEvent> attendEventConsumer;

	public AttendEventPublisher() {
		attendEventFlux = Flux.create(fluxSink -> attendEventConsumer = fluxSink::next);
	}

	public void publish(AttendEvent eventMessage) {
		this.attendEventConsumer.accept(eventMessage);
	}

	public Flux<AttendEvent> attendEventFlux() {
		return this.attendEventFlux;
	}
}
