package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.event.scheme.StudyEvent;
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

	public Mono<ResponseEntity<Void>> create(Study study) {
		return this.repository.create(study)
		                      .doOnNext(this::publishEvent)
		                      .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                      .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	private void publishEvent(Study study) {
		StudyEventPublisher.publishEvent(StudyEvent.builder().study(study)
		                                           .action(StudyEvent.ActionType.CREATE)
		                                           .build());
	}
}
