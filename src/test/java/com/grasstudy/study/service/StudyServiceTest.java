package com.grasstudy.study.service;

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
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = {TestChannelBinderConfiguration.class, StudyApplication.class})
class StudyServiceTest {

	@Autowired
	StudyService studyService;

	@Autowired
	OutputDestination outputDestination;

	@Autowired
	CompositeMessageConverter compositeMessageConverter;

	@Test
	void create() {
		Study study = Study.builder()
		                   .id(1l)
		                   .name("test-study")
		                   .interestTags(List.of("java", "msa"))
		                   .build();

		StepVerifier.create(studyService.create(study))
		            .expectNextCount(1)
		            .verifyComplete();

		Message<byte[]> receive = outputDestination.receive(1000, "study-create-event");
		StudyCreateEvent studyCreateEvent = (StudyCreateEvent) compositeMessageConverter.fromMessage(receive, StudyCreateEvent.class);
		Assertions.assertThat(studyCreateEvent)
		          .matches(v -> v.getStudy().getId().equals(1l), "Id Equals")
		          .matches(v -> v.getStudy().getName().equals("test-study"), "Name Equals")
		          .matches(v -> v.getStudy().getInterestTags().containsAll(List.of("java", "msa")), "Interests Equals");
	}
}