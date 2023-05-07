package com.grasstudy.study.repository;

import com.grasstudy.study.entity.Crew;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CrewRepository extends ReactiveCrudRepository<Crew, String> {

	Flux<Crew> findAllByStudyId(String studyId);
	Mono<Void> deleteAllByStudyId(String studyId);
	Mono<Crew> findByStudyIdAndUserId(String studyId, String userId);
}
