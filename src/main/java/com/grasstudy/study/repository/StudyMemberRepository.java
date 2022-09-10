package com.grasstudy.study.repository;

import com.grasstudy.study.entity.StudyMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudyMemberRepository extends ReactiveCrudRepository<StudyMember, Long> {

	Flux<StudyMember> findAllByStudyId(Long studyId);
}
