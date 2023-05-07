package com.grasstudy.study.controller;

import com.grasstudy.study.dto.StudyCreateParam;
import com.grasstudy.study.dto.StudyModifyParam;
import com.grasstudy.study.service.StudyService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "/study")
@RequiredArgsConstructor
public class StudyController {

	private final StudyService studyService;

	//todo: Response 재정의
	@RequestMapping(method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> create(@AuthenticationPrincipal Claims principal,
	                                         @RequestBody StudyCreateParam studyCreateParam) {
		return studyService.create((String) principal.get("userId"), studyCreateParam.toEntity())
		                   .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/{studyId}", method = RequestMethod.PUT)
	public Mono<ResponseEntity<Void>> modify(@PathVariable String studyId,
	                                         @RequestBody StudyModifyParam studyModifyParam) {
		return studyService.modify(studyModifyParam.toEntity(studyId))
		                   .map(unused -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/{studyId}", method = RequestMethod.DELETE)
	public Mono<ResponseEntity<Void>> delete(@PathVariable String studyId) {
		return studyService.delete(studyId)
		                   .map(unused -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

}
