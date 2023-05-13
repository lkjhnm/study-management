package com.grasstudy.noticer

import com.grasstudy.noticer.attend.scheme.event.AttendEvent
import org.reactivestreams.Subscriber
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
class InboundEventConfiguration {

	// todo: Study CreateEvent Consumer
	// todo: Study ModifyEvent Consumer

	@Bean
	fun attendEventSubscriber(attendEventSwitch: Subscriber<AttendEvent>): Consumer<Flux<AttendEvent>> {
		return Consumer { v -> v.subscribe(attendEventSwitch) }
	}
}