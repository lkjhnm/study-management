package com.grasstudy.study.repository;

import com.grasstudy.study.R2DBCConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import({R2DBCConfiguration.class, StudyRepoService.class})
class StudyRepoServiceTest {

	@Autowired
	StudyRepoService studyRepoService;

	@Test
	void fetchOne() {
		StepVerifier.create(studyRepoService.fetchOne(1l).log())
				.expectNextCount(1)
				.verifyComplete();
	}
}