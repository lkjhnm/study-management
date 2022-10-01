package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.StudyCreateEvent;
import com.grasstudy.study.repository.StudyRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepoService repository;
	private final StudyEventPublisher studyEventPublisher;

	public Mono<ResponseEntity<Void>> create(Study study) {
		return this.repository.create(study)
		                      .doOnNext(this::publishCreateEvent)
		                      .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                      .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	private void publishCreateEvent(Study study) {
		studyEventPublisher.publish(StudyCreateEvent.builder().study(study)
		                                            .build());
	}
}
