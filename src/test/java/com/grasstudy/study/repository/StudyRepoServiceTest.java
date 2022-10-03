package com.grasstudy.study.repository;

import com.grasstudy.study.R2DBCConfiguration;
import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyMember;
import com.grasstudy.study.mock.MockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@DataR2dbcTest
@Import({R2DBCConfiguration.class, StudyRepoService.class})
class StudyRepoServiceTest {

	@Autowired
	StudyRepoService studyRepoService;

	@Autowired
	StudyRepository studyRepository;

	@Autowired
	StudyMemberRepository memberRepository;

	private Mono<String> saveMockStudy() {
		return studyRepoService.save(MockData.newStudy())
		                       .map(Study::getId);
	}

	private Mono<String> saveMockStudyWithMember() {
		return saveMockStudy()
				.flatMap(studyId -> memberRepository.save(MockData.newStudyMember(studyId, StudyMember.Authority.MEMBER)))
				.map(StudyMember::getStudyId);
	}

	@Test
	void insert() {
		studyRepoService.save(MockData.newStudy())
		                .map(Study::getId)
		                .flatMap(studyRepository::findById)
		                .as(StepVerifier::create)
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
				.as(StepVerifier::create)
				.expectNextMatches(v -> v.getName().equals("Modified Name"))
				.verifyComplete();
	}

	@Test
	void fetchOne() {
		saveMockStudyWithMember()
				.flatMap(studyRepoService::fetchOne)
				.as(StepVerifier::create)
				.expectNextMatches(v -> Objects.nonNull(v.getMembers()))
				.verifyComplete();
	}

	@Test
	void delete() {
		saveMockStudyWithMember()
		               .flatMap(studyRepoService::delete)
		               .flatMap(studyRepoService::fetchOne)
		               .as(StepVerifier::create)
		               .expectNextCount(0)
		               .verifyComplete();
	}
}