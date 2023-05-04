package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
import com.grasstudy.study.repository.StudyRepoDelegator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepoDelegator repository;
	private final StudyEventPublisher studyEventPublisher;

	public Mono<Study> create(String userId, Study study) {
		return this.repository.create(study, userId)
		                      .doOnNext(this::publishCreateEvent);
	}

	public Mono<Study> modify(Study study) {
		return this.repository.modify(study);
	}

	public Mono<String> delete(String studyId) {
		return this.repository.delete(studyId);
	}

	public Mono<Boolean> exist(String studyId) {
		return this.repository.exist(studyId);
	}

	private void publishCreateEvent(Study study) {
		studyEventPublisher.publish(StudyCreateEvent.builder().study(study)
		                                            .build());
	}
}
