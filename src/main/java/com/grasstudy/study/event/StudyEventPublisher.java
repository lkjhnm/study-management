package com.grasstudy.study.event;

import com.grasstudy.study.event.scheme.StudyEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class StudyEventPublisher {

	private static final Sinks.Many<StudyEvent> STUDY_SINK = Sinks.many().unicast().onBackpressureBuffer();

	public static void publishEvent(StudyEvent studyEvent) {
		Sinks.EmitResult emitResult = STUDY_SINK.tryEmitNext(studyEvent);
		//todo: handle emit result
	}

	public static Flux<StudyEvent> studyEventFlux() {
		return STUDY_SINK.asFlux();
	}
}
