package com.grasstudy.study.repository;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRepoService {

	private final StudyRepository studyRepository;
	private final StudyMemberRepository memberRepository;

	public Mono<Study> create(Study study) {
		return this.studyRepository.save(study);
	}

	public Mono<Study> fetchOne(Long studyId) {
		Mono<Study> studyGroup = studyRepository.findById(studyId);
		Mono<List<StudyMember>> members = memberRepository.findAllByStudyId(studyId)
		                                                  .collectList();
		return Mono.zip(studyGroup, members)
				.map(t ->{
					Study study = t.getT1();
					study.setMembers(t.getT2());
					return study;
				});
	}
}
