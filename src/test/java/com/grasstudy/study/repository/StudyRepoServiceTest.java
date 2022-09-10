package com.grasstudy.study.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class StudyRepoServiceTest {

	@Autowired
	StudyRepository repository;

	@Autowired
	StudyRepoService studyRepoService;

	@Test
	void fetchOne() {
		StepVerifier.create(studyRepoService.fetchOne(1l).log())
				.expectNextCount(1)
				.verifyComplete();
	}
}