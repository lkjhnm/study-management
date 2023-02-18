package com.grasstudy.study.event;

import com.grasstudy.study.StudyApplication;
import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
import com.grasstudy.study.event.scheme.StudyJoinEvent;
import com.grasstudy.study.test.mock.MockData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.test.context.ContextConfiguration;

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
	public void create_event_publish() {
		Study mockStudy = MockData.study("test-study-id");
		studyEventPublisher.publish(StudyCreateEvent.builder()
		                                            .study(mockStudy)
		                                            .build());

		Message<byte[]> receive = outputDestination.receive(1000, "study-create-event");
		Assertions.assertThat(receive).isNotNull();
		StudyCreateEvent studyCreateEvent = (StudyCreateEvent) compositeMessageConverter.fromMessage(receive, StudyCreateEvent.class);
		Assertions.assertThat(studyCreateEvent)
		          .matches(v -> v.getStudy().getId().equals(mockStudy.getId()), "Id Equals")
		          .matches(v -> v.getStudy().getName().equals(mockStudy.getName()), "Name Equals")
		          .matches(v -> v.getStudy().getInterestTags()
		                         .containsAll(mockStudy.getInterestTags()), "Interests Equals");
	}

	@Test
	void join_event_publish() {
		StudyJoin mockStudyJoin = MockData.studyJoin();
		studyEventPublisher.publish(StudyJoinEvent.builder()
		                                          .studyJoin(mockStudyJoin)
		                                          .build());

		Message<byte[]> receive = outputDestination.receive(1000, "study-join-event");
		Assertions.assertThat(receive).isNotNull();
		StudyJoinEvent studyJoinEvent = (StudyJoinEvent) compositeMessageConverter.fromMessage(receive, StudyJoinEvent.class);
		Assertions.assertThat(studyJoinEvent)
		          .matches(v -> v.getStudyJoin().getStudyId().equals(mockStudyJoin.getStudyId()), "StudyId Equals")
		          .matches(v -> v.getStudyJoin().getUserId().equals(mockStudyJoin.getUserId()), "UserId Equals")
		          .matches(v -> v.getStudyJoin().getState().equals(StudyJoin.JoinState.WAIT), "State Equals");
	}
}
