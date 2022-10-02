package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.mock.MockData;
import com.grasstudy.study.repository.StudyRepoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
		Study study = MockData.newStudy();
		Mockito.when(studyRepoService.save(any())).thenReturn(Mono.just(study));
		StepVerifier.create(studyService.create(study))
		            .expectNext(ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		            .verifyComplete();
		Mockito.verify(studyEventPublisher, Mockito.times(1)).publish(any());
	}
}