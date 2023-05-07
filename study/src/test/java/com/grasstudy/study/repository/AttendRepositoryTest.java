package com.grasstudy.study.repository;

import com.grasstudy.R2DBCConfiguration;
import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.test.RxTransactional;
import com.grasstudy.study.test.mock.MockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@RxTransactional
@Import({R2DBCConfiguration.class})
class AttendRepositoryTest {

	@Autowired
	AttendRepository attendRepository;

	@Test
	void save() {
		Attend mockAttend = MockData.attend(Attend.AttendState.WAIT);
		attendRepository.save(mockAttend)
		                .as(StepVerifier::create)
		                .expectNext(mockAttend)
		                .verifyComplete();
	}

	//todo: attendRepository.findAllById

}