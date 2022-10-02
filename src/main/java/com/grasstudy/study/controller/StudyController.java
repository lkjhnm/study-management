package com.grasstudy.study.controller;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class StudyController {

	private final StudyService studyService;

	@RequestMapping(value = "/study", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> create(Study study) {
		return studyService.create(study);
	}

	@RequestMapping(value = "/study", method = RequestMethod.PUT)
	public Mono<ResponseEntity<Void>> modify(Study study) {
		return studyService.modify(study);
	}
}
