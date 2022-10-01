package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.StudyEventPublisher;
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
		Study study = Study.builder()
		                   .id(1l)
		                   .name("test-study")
		                   .interestTags(List.of("java", "msa"))
		                   .build();
		Mockito.when(studyRepoService.create(any())).thenReturn(Mono.just(study));
		StepVerifier.create(studyService.create(study))
		            .expectNext(ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		            .verifyComplete();
	}
}