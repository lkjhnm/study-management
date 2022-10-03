package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
import com.grasstudy.study.repository.StudyRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepoService repository;
	private final StudyEventPublisher studyEventPublisher;

	public Mono<Study> create(Study study) {
		return this.repository.save(study)
		                      .doOnNext(this::publishCreateEvent);
	}

	public Mono<Study> modify(Study study) {
		return this.repository.save(study);
	}

	public Mono<String> delete(String studyId) {
		return this.repository.delete(studyId);
	}

	private void publishCreateEvent(Study study) {
		studyEventPublisher.publish(StudyCreateEvent.builder().study(study)
		                                            .build());
	}
}
