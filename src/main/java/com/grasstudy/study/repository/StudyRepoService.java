package com.grasstudy.study.repository;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyRepoService {

	private final StudyRepository studyRepository;
	private final StudyMemberRepository memberRepository;

	@Transactional
	public Mono<Study> create(Study study, String userId) {
		return this.save(study)
		           .flatMap(v -> this.saveAll(defaultMembers(userId, v.getId()))
		                             .collectList())
		           .map(unused -> study);
	}

	@Transactional
	public Mono<Study> modify(Study study) {
		return Mono.zip(
				this.save(study),
				Optional.ofNullable(study.getMembers())
				        .map(this::saveAll).map(Flux::collectList)
				        .orElse(Mono.empty()),
				this::studyWithMembers);
	}

	Mono<Study> save(Study study) {
		return this.studyRepository.save(study);
	}

	Flux<StudyMember> saveAll(List<StudyMember> members) {
		return this.memberRepository.saveAll(members);
	}

	public Mono<Study> fetchOne(String studyId) {
		Mono<Study> studyGroup = studyRepository.findById(studyId);
		Mono<List<StudyMember>> members = memberRepository.findAllByStudyId(studyId)
		                                                  .collectList();
		return Mono.zip(studyGroup, members, this::studyWithMembers);
	}

	public Mono<String> delete(String studyId) {
		return Mono.zip(studyRepository.deleteById(studyId), memberRepository.deleteAllByStudyId(studyId))
		           .map(unused -> studyId);
	}

	private List<StudyMember> defaultMembers(String userId, String studyId) {
		return List.of(StudyMember.builder().studyId(studyId).userId(userId)
		                          .authority(StudyMember.Authority.OWNER)
		                          .build());
	}

	private Study studyWithMembers(Study study, List<StudyMember> members) {
		study.setMembers(members);
		return study;
	}
}
