package com.grasstudy.study.event;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
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
@Import({StudyEventPublisher.class, TestChannelBinderConfiguration.class})
public class StudyEventPublisherTest {

	@Configuration
	@EnableAutoConfiguration
	static class OutboundConfiguration {
		@Bean
		public Supplier<Flux<StudyCreateEvent>> studyCreateEventPublisher(StudyEventPublisher studyEventPublisher) {
			return () -> studyEventPublisher.createEventFlux();
		}
	}

	@Autowired
	StudyEventPublisher studyEventPublisher;

	@Autowired
	OutputDestination outputDestination;

	@Autowired
	CompositeMessageConverter compositeMessageConverter;

	@Test
	public void create_event_publish() {
		Study mockStudy = MockData.study("test-study-id");
		studyEventPublisher.publish(StudyCreateEvent.builder()
		                                            .study(mockStudy)
		                                            .build());

		Message<byte[]> receive = outputDestination.receive(1000, "studyCreateEventPublisher-out-0");
		Assertions.assertThat(receive).isNotNull();
		StudyCreateEvent studyCreateEvent = (StudyCreateEvent) compositeMessageConverter.fromMessage(receive, StudyCreateEvent.class);
		Assertions.assertThat(studyCreateEvent)
		          .matches(v -> v.getStudy().getId().equals(mockStudy.getId()), "Id Equals")
		          .matches(v -> v.getStudy().getName().equals(mockStudy.getName()), "Name Equals")
		          .matches(v -> v.getStudy().getInterestTags()
		                         .containsAll(mockStudy.getInterestTags()), "Interests Equals");
	}
}
