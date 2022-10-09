package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyMember;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.mock.MockData;
import com.grasstudy.study.repository.StudyRepoService;
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
	StudyRepoService studyRepoService;

	@Mock
	StudyEventPublisher studyEventPublisher;

	@Test
	void create() {
		Study mockStudy = MockData.study("test-study-id");
		Mockito.when(studyRepoService.create(any(), any())).thenReturn(Mono.just(mockStudy));

		StepVerifier.create(studyService.create("test-study-owner", mockStudy))
		            .expectNext(mockStudy)
		            .verifyComplete();
		Mockito.verify(studyEventPublisher, Mockito.times(1)).publish(any());
	}

	@Test
	void modify() {
		Study mockStudy = MockData.study("test-study-id");
		StudyMember mockMember = MockData.studyMember("test-study-id", StudyMember.Authority.OWNER);
		mockStudy.setMembers(List.of(mockMember));
		Mockito.when(studyRepoService.modify(any())).thenReturn(Mono.just(mockStudy));

		StepVerifier.create(studyService.modify(mockStudy))
		            .expectNext(mockStudy)
		            .verifyComplete();
	}

	@Test
	void delete() {
		Study study = MockData.study("test-study-id");
		Mockito.when(studyRepoService.delete(any())).thenReturn(Mono.just(study.getId()));
		StepVerifier.create(studyService.delete(study.getId()))
		            .expectNext("test-study-id")
		            .verifyComplete();
	}
}