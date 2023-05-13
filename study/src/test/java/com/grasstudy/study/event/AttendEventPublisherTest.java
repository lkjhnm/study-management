package com.grasstudy.study.event;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.event.scheme.AttendEvent;
import com.grasstudy.study.test.mock.MockData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@ExtendWith(SpringExtension.class)
@Import({AttendEventPublisher.class, TestChannelBinderConfiguration.class})
public class AttendEventPublisherTest {

	@Configuration
	@EnableAutoConfiguration
	static class OutboundConfiguration {
		@Bean
		public Supplier<Flux<AttendEvent>> attendEventPublisher(AttendEventPublisher attendEventPublisher) {
			return () -> attendEventPublisher.attendEventFlux();
		}
	}

	@Autowired
	AttendEventPublisher attendEventPublisher;

	@Autowired
	OutputDestination outputDestination;

	@Autowired
	CompositeMessageConverter compositeMessageConverter;

	@Test
	void attend_request_event_publish() {
		Attend mockAttend = MockData.attend(Attend.AttendState.WAIT);
		attendEventPublisher.publish(AttendEvent.builder()
		                                        .attend(mockAttend)
		                                        .build());

		Message<byte[]> receive = outputDestination.receive(1000, "attendEventPublisher-out-0");
		Assertions.assertThat(receive).isNotNull();
		AttendEvent attendRequestEvent = (AttendEvent) compositeMessageConverter.fromMessage(receive, AttendEvent.class);
		Assertions.assertThat(attendRequestEvent)
		          .matches(v -> v.getAttend().getStudyId().equals(mockAttend.getStudyId()), "StudyId Equals")
		          .matches(v -> v.getAttend().getUserId().equals(mockAttend.getUserId()), "UserId Equals")
		          .matches(v -> v.getAttend().getState().equals(Attend.AttendState.WAIT), "State Equals");
	}
}
