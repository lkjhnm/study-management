package com.grasstudy.study.repository;

import com.grasstudy.R2DBCConfiguration;
import com.grasstudy.study.entity.Study;
import com.grasstudy.study.test.RxTransactional;
import com.grasstudy.study.test.TxStepVerifier;
import com.grasstudy.study.entity.Crew;
import com.grasstudy.study.test.mock.MockData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyList;

@DataR2dbcTest
@RxTransactional
@Import({R2DBCConfiguration.class, StudyRepoDelegator.class})
class StudyRepoDelegatorTest {

	@Autowired
	StudyRepoDelegator studyRepoDelegator;

	@Autowired
	StudyRepository studyRepository;

	@SpyBean
	CrewRepository crewRepository;

	private Mono<String> saveMockStudy() {
		return studyRepoDelegator.save(MockData.study())
		                         .map(Study::getId);
	}

	private Mono<String> saveMockStudyWithMember() {
		return saveMockStudy()
				.flatMap(studyId -> crewRepository.save(MockData.crew(studyId, Crew.Authority.MEMBER)))
				.map(Crew::getStudyId);
	}

	@Test
	void create() {
		studyRepoDelegator.create(MockData.study(), "test-user-id")
		                  .map(Study::getId)
		                  .flatMap(studyRepoDelegator::fetchOne)
		                  .as(TxStepVerifier::create)
		                  .expectNextMatches(v -> Objects.nonNull(v.getId()) && v.getCrews().size() == 1)
		                  .verifyComplete();
	}

	@Test
	void create_fail_and_transaction_test() {
		Mockito.when(crewRepository.saveAll(anyList())).thenReturn(Flux.error(new RuntimeException("Test")));

		studyRepoDelegator.create(MockData.study(), "test-user-id")
		                  .map(Study::getId)
		                  .as(TxStepVerifier::create)
		                  .expectError().verify();

		studyRepository.findAll()
		               .as(TxStepVerifier::create)
		               .expectNextCount(0)
		               .verifyComplete();
	}

	@Test
	void modify() {
		saveMockStudyWithMember()
				.map(id -> Study.builder().id(id)
				                .name("changed")
				                .crews(List.of(
						                Crew.builder().studyId(id).userId("owner")
						                    .authority(Crew.Authority.OWNER).build(),
						                Crew.builder().studyId(id).userId("member")
						                    .authority(Crew.Authority.MEMBER).build()
				                )).build()
				).flatMap(studyRepoDelegator::modify)
				.as(TxStepVerifier::create)
				.expectNextMatches(v -> v.getName().equals("changed") && v.getCrews().size() == 2)
				.verifyComplete();
	}

	@Test
	void insert() {
		studyRepoDelegator.save(MockData.study())
		                  .map(Study::getId)
		                  .flatMap(studyRepository::findById)
		                  .as(TxStepVerifier::create)
		                  .expectNextCount(1)
		                  .verifyComplete();
	}

	@Test
	void update() {
		saveMockStudy()
				.flatMap(studyRepository::findById)
				.map(study -> {
					study.setName("Modified Name");
					return study;
				})
				.flatMap(studyRepository::save)
				.flatMap(study -> studyRepository.findById(study.getId()))
				.as(TxStepVerifier::create)
				.expectNextMatches(v -> v.getName().equals("Modified Name"))
				.verifyComplete();
	}

	@Test
	void fetchOne() {
		saveMockStudyWithMember()
				.flatMap(studyRepoDelegator::fetchOne)
				.as(TxStepVerifier::create)
				.expectNextMatches(v -> Objects.nonNull(v.getCrews()))
				.verifyComplete();
	}

	@Test
	void delete() {
		saveMockStudyWithMember()
				.flatMap(studyRepoDelegator::delete)
				.flatMap(studyRepoDelegator::fetchOne)
				.as(TxStepVerifier::create)
				.expectNextCount(0)
				.verifyComplete();
	}
}