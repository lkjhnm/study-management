package com.grasstudy.study.repository;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.Crew;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyRepoDelegator {

	private final StudyRepository studyRepository;
	private final CrewRepository crewRepository;

	@Transactional
	public Mono<Study> create(Study study, String userId) {
		return this.save(study)
		           .flatMap(v -> this.saveAll(defaultCrews(userId, v.getId()))
		                             .collectList())
		           .map(unused -> study);
	}

	@Transactional
	public Mono<Study> modify(Study study) {
		return Mono.zip(
				this.save(study),
				Optional.ofNullable(study.getCrews())
				        .map(this::saveAll).map(Flux::collectList)
				        .orElse(Mono.empty()),
				this::merge);
	}

	Mono<Study> save(Study study) {
		return this.studyRepository.save(study);
	}

	Flux<Crew> saveAll(List<Crew> crews) {
		return this.crewRepository.saveAll(crews);
	}

	@Transactional
	public Mono<Study> fetchOne(String studyId) {
		Mono<Study> studyGroup = studyRepository.findById(studyId);
		Mono<List<Crew>> crews = crewRepository.findAllByStudyId(studyId)
		                                       .collectList();
		return Mono.zip(studyGroup, crews, this::merge);
	}

	@Transactional
	public Mono<String> delete(String studyId) {
		return Mono.zip(studyRepository.deleteById(studyId), crewRepository.deleteAllByStudyId(studyId))
		           .map(unused -> studyId);
	}

	public Mono<Boolean> exist(String studyId) {
		return this.studyRepository.existsById(studyId);
	}

	private List<Crew> defaultCrews(String userId, String studyId) {
		return List.of(Crew.builder().studyId(studyId).userId(userId)
		                   .authority(Crew.Authority.OWNER)
		                   .build());
	}

	private Study merge(Study study, List<Crew> crews) {
		study.setCrews(crews);
		return study;
	}
}
