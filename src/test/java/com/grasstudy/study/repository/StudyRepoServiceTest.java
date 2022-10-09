package com.grasstudy.study.repository;

import com.grasstudy.study.R2DBCConfiguration;
import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyMember;
import com.grasstudy.study.mock.MockData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyList;

@DataR2dbcTest
@Import({R2DBCConfiguration.class, StudyRepoService.class})
class StudyRepoServiceTest {

	@Autowired
	StudyRepoService studyRepoService;

	@Autowired
	StudyRepository studyRepository;

	@SpyBean
	StudyMemberRepository memberRepository;

	private Mono<String> saveMockStudy() {
		return studyRepoService.save(MockData.study())
		                       .map(Study::getId);
	}

	private Mono<String> saveMockStudyWithMember() {
		return saveMockStudy()
				.flatMap(studyId -> memberRepository.save(MockData.studyMember(studyId, StudyMember.Authority.MEMBER)))
				.map(StudyMember::getStudyId);
	}

	@Test
	void create() {
		studyRepoService.create(MockData.study(), "test-user-id")
		                .map(Study::getId)
		                .flatMap(studyRepoService::fetchOne)
		                .as(StepVerifier::create)
		                .expectNextMatches(v -> Objects.nonNull(v.getId()) && v.getMembers().size() == 1)
		                .verifyComplete();
	}

	@Test
	void create_fail_and_transaction_test() {
		Mockito.when(memberRepository.saveAll(anyList())).thenReturn(Flux.error(new RuntimeException("Test")));

		studyRepoService.create(MockData.study(), "test-user-id")
		                .map(Study::getId)
		                .as(StepVerifier::create)
		                .expectError().verify();

		studyRepository.findAll()
		               .as(StepVerifier::create)
		               .expectNextCount(0)
		               .verifyComplete();
	}

	@Test
	void modify() {
		saveMockStudyWithMember()
				.map(id -> Study.builder().id(id)
				                .name("changed")
				                .members(List.of(
						                StudyMember.builder().studyId(id).userId("owner")
						                           .authority(StudyMember.Authority.OWNER).build(),
						                StudyMember.builder().studyId(id).userId("member")
						                           .authority(StudyMember.Authority.MEMBER).build()
				                )).build()
				).flatMap(studyRepoService::modify)
				.as(StepVerifier::create)
				.expectNextMatches(v -> v.getName().equals("changed") && v.getMembers().size() == 2)
				.verifyComplete();
	}

	@Test
	void insert() {
		studyRepoService.save(MockData.study())
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