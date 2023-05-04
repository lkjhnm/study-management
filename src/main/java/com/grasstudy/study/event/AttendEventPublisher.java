package com.grasstudy.attend.event;

import com.grasstudy.attend.event.scheme.AttendRequestEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Service
public class AttendEventPublisher {

	private final Flux<AttendRequestEvent> attendCreateEventFlux;

	private Consumer<AttendRequestEvent> attendCreateEventConsumer;

	public AttendEventPublisher() {
		attendCreateEventFlux = Flux.create(fluxSink -> attendCreateEventConsumer = fluxSink::next);
	}

	public void publish(AttendRequestEvent eventMessage) {
		this.attendCreateEventConsumer.accept(eventMessage);
	}

	public Flux<AttendRequestEvent> attendCreateEventFlux() {
		return this.attendCreateEventFlux;
	}
}
