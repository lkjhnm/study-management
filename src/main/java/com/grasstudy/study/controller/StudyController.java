package com.grasstudy.study.controller;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.service.StudyJoinService;
import com.grasstudy.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "/study")
@RequiredArgsConstructor
public class StudyController {

	private final StudyService studyService;
	private final StudyJoinService studyJoinService;

	@RequestMapping(method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> create(Study study) {
		return studyService.create(study)
		                   .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Mono<ResponseEntity<Void>> modify(Study study) {
		return studyService.modify(study)
		                   .map(unused -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public Mono<ResponseEntity<Void>> delete(String studyId) {
		return studyService.delete(studyId)
		                   .map(unused -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> join(String studyId, String userId) {
		return studyJoinService.join(studyId, userId)
		                       .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                       .onErrorReturn(ResponseEntity.internalServerError().build());
	}

}
