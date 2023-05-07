package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.entity.Crew;
import com.grasstudy.study.repository.StudyRepoDelegator;
import com.grasstudy.study.test.mock.MockData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class StudyServiceTest {

	@InjectMocks
	StudyService studyService;

	@Mock
	StudyRepoDelegator studyRepoDelegator;

	@Mock
	StudyEventPublisher studyEventPublisher;

	@Test
	void create() {
		Study mockStudy = MockData.study("test-study-id");
		Mockito.when(studyRepoDelegator.create(any(), any())).thenReturn(Mono.just(mockStudy));

		StepVerifier.create(studyService.create("test-study-owner", mockStudy))
		            .expectNext(mockStudy)
		            .verifyComplete();
		Mockito.verify(studyEventPublisher, Mockito.times(1)).publish(any());
	}

	@Test
	void modify() {
		Study mockStudy = MockData.study("test-study-id");
		Crew mockMember = MockData.managedCrew("test-study-id", Crew.Authority.OWNER);
		mockStudy.setCrews(List.of(mockMember));
		Mockito.when(studyRepoDelegator.modify(any())).thenReturn(Mono.just(mockStudy));

		StepVerifier.create(studyService.modify(mockStudy))
		            .expectNext(mockStudy)
		            .verifyComplete();
	}

	@Test
	void delete() {
		Study study = MockData.study("test-study-id");
		Mockito.when(studyRepoDelegator.delete(any())).thenReturn(Mono.just(study.getId()));
		StepVerifier.create(studyService.delete(study.getId()))
		            .expectNext("test-study-id")
		            .verifyComplete();
	}
}