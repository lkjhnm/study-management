package com.grasstudy.study.event;

import com.grasstudy.study.StudyApplication;
import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = {TestChannelBinderConfiguration.class, StudyApplication.class})
public class StudyEventPublisherTest {

	@Autowired
	StudyEventPublisher studyEventPublisher;

	@Autowired
	OutputDestination outputDestination;

	@Autowired
	CompositeMessageConverter compositeMessageConverter;

	@Test
	public void createEventTest() {
		studyEventPublisher.publish(StudyCreateEvent.builder()
		                                            .study(Study.builder()
		                                                        .id("test-id")
		                                                        .name("test-study")
		                                                        .interestTags(List.of("java", "msa"))
		                                                        .build())
		                                            .build());

		Message<byte[]> receive = outputDestination.receive(1000, "study-create-event");
		Assertions.assertThat(receive).isNotNull();
		StudyCreateEvent studyCreateEvent = (StudyCreateEvent) compositeMessageConverter.fromMessage(receive, StudyCreateEvent.class);
		Assertions.assertThat(studyCreateEvent)
		          .matches(v -> v.getStudy().getId().equals("test-id"), "Id Equals")
		          .matches(v -> v.getStudy().getName().equals("test-study"), "Name Equals")
		          .matches(v -> v.getStudy().getInterestTags().containsAll(List.of("java", "msa")), "Interests Equals");
	}
}
