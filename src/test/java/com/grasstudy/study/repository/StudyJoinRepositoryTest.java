package com.grasstudy.study.repository;

import com.grasstudy.study.R2DBCConfiguration;
import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.mock.MockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import({R2DBCConfiguration.class})
class StudyJoinRepositoryTest {

	@Autowired
	StudyJoinRepository studyJoinRepository;

	@Test
	void insert() {
		StudyJoin mockStudyJoin = MockData.studyJoin();
		studyJoinRepository.save(mockStudyJoin)
		                   .as(StepVerifier::create)
		                   .expectNext(mockStudyJoin)
		                   .verifyComplete();
	}

}