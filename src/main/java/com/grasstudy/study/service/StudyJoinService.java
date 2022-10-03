package com.grasstudy.study.service;

import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.StudyJoinEvent;
import com.grasstudy.study.repository.StudyJoinRepository;
import com.grasstudy.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudyJoinService {

	private final StudyJoinRepository joinRepository;
	private final StudyRepository studyRepository;
	private final StudyEventPublisher studyEventPublisher;

	public Mono<StudyJoin> join(String studyId, String userId) {
		return studyRepository.findById(studyId)
		                      .switchIfEmpty(Mono.error(NoSuchElementException::new))
		                      .map(unused -> StudyJoin.builder()
		                                              .studyId(studyId).userId(userId)
		                                              .state(StudyJoin.JoinState.WAIT)
		                                              .build())
		                      .flatMap(joinRepository::save)
		                      .doOnNext(this::publishJoinEvent);
	}

	private void publishJoinEvent(StudyJoin studyJoin) {
		this.studyEventPublisher.publish(StudyJoinEvent.builder()
		                                               .studyJoin(studyJoin)
		                                               .build());
	}
}
