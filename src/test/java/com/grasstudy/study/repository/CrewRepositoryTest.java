package com.grasstudy.study.repository;

import com.grasstudy.R2DBCConfiguration;
import com.grasstudy.study.entity.Crew;
import com.grasstudy.study.test.RxTransactional;
import com.grasstudy.study.test.TxStepVerifier;
import com.grasstudy.study.test.mock.MockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.List;

@DataR2dbcTest
@RxTransactional
@Import({R2DBCConfiguration.class})
class CrewRepositoryTest {

	private static final String TEST_STUDY_ID = "test-study-id";

	@Autowired
	CrewRepository crewRepository;

	@BeforeEach
	void init() {
		crewRepository.saveAll(
				              List.of(
						              MockData.transientCrew(TEST_STUDY_ID, Crew.Authority.OWNER, "owner-id"),
						              MockData.transientCrew(TEST_STUDY_ID, Crew.Authority.MEMBER, "member-id")
				              ))
		              .blockLast();
	}

	@AfterEach
	void destroy() {
		crewRepository.deleteAll().block();
	}

	@Test
	void find_all_by_study_id() {
		crewRepository.findAllByStudyId(TEST_STUDY_ID).log()
		              .as(StepVerifier::create)
		              .expectNextCount(2)
		              .verifyComplete();
	}

	@Test
	void delete_all_by_study_id() {
		crewRepository.deleteAllByStudyId(TEST_STUDY_ID).log()
		              .as(TxStepVerifier::create)
		              .expectComplete()
		              .verify();
	}

	@Test
	void find_by_study_id_and_user_id() {
		crewRepository.findByStudyIdAndUserId(TEST_STUDY_ID, "owner-id").log()
		              .as(StepVerifier::create)
		              .expectNextMatches(v ->
				              v.getStudyId().equals(TEST_STUDY_ID) &&
						              v.getUserId().equals("owner-id") &&
						              v.getAuthority() == Crew.Authority.OWNER
		              )
		              .verifyComplete();

		crewRepository.findByStudyIdAndUserId(TEST_STUDY_ID, "member-id").log()
		              .as(StepVerifier::create)
		              .expectNextMatches(v ->
				              v.getStudyId().equals(TEST_STUDY_ID) &&
						              v.getUserId().equals("member-id") &&
						              v.getAuthority() == Crew.Authority.MEMBER
		              )
		              .verifyComplete();

		crewRepository.findByStudyIdAndUserId("there is no study", "owner-id").log()
		              .as(StepVerifier::create)
		              .expectComplete()
		              .verify();

		crewRepository.findByStudyIdAndUserId(TEST_STUDY_ID, "ghost-user-id").log()
		              .as(StepVerifier::create)
		              .expectComplete()
		              .verify();
	}
}