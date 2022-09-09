package com.grasstudy.group.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudyGroupRepoServiceTest {

	@Autowired
	StudyGroupRepository repository;

	@Autowired
	StudyGroupRepoService studyGroupRepoService;

	@Test
	void fetchOne() {
		StepVerifier.create(studyGroupRepoService.fetchOne(1l).log())
				.expectNextCount(1)
				.verifyComplete();
	}
}