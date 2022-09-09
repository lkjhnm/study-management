package com.grasstudy.group.repository;

import com.grasstudy.group.entity.StudyMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudyMemberRepository extends ReactiveCrudRepository<StudyMember, Long> {

	Flux<StudyMember> findAllByStudyGroupId(Long studyId);
}
