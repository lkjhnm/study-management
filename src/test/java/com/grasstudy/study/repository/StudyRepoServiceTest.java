package com.grasstudy.study.repository;

import com.grasstudy.study.R2DBCConfiguration;
import com.grasstudy.study.entity.StudyMember;
import com.grasstudy.study.mock.MockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
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

	@Test
	void insert() {
		StepVerifier.create(studyRepoService.save(MockData.newStudy())
		                                    .flatMap(study -> studyRepository.findById(study.getId())))
		            .expectNextCount(1)
		            .verifyComplete();
	}

	@Test
	void update() {
		StepVerifier.create(
				            studyRepository.findAll().take(1)
				                           .map(study -> {
					                           study.setName("Modified Name");
					                           return study;
				                           })
				                           .flatMap(studyRepository::save)
				                           .flatMap(study -> studyRepository.findById(study.getId()))
		            )
		            .expectNextMatches(v -> v.getName().equals("Modified Name"))
		            .verifyComplete();
	}

	@Test
	void fetchOne() {
		StepVerifier.create(
				            studyRepository.save(MockData.newStudy())
				                           .flatMap(study -> memberRepository.save(MockData.newStudyMember(study.getId(), StudyMember.Authority.MEMBER)))
				                           .flatMap(studyMember -> studyRepoService.fetchOne(studyMember.getStudyId()))
		            )
		            .expectNextMatches(v -> Objects.nonNull(v.getMembers()))
		            .verifyComplete();
	}
}