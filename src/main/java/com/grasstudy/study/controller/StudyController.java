package com.grasstudy.study.controller;

import com.grasstudy.study.dto.StudyCreateRequest;
import com.grasstudy.study.dto.StudyModifyRequest;
import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.service.StudyJoinService;
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

import java.util.List;

@Controller
@RequestMapping(value = "/study")
@RequiredArgsConstructor
public class StudyController {

	private final StudyService studyService;
	private final StudyJoinService studyJoinService;

	//todo: Response 재정의
	@RequestMapping(method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> create(@AuthenticationPrincipal Claims principal,
	                                         @RequestBody StudyCreateRequest studyCreate) {
		return studyService.create((String) principal.get("userId"), studyCreate.toEntity())
		                   .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/{studyId}", method = RequestMethod.PUT)
	public Mono<ResponseEntity<Void>> modify(@PathVariable String studyId,
	                                         @RequestBody StudyModifyRequest studyModify) {
		return studyService.modify(studyModify.toEntity(studyId))
		                   .map(unused -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/{studyId}", method = RequestMethod.DELETE)
	public Mono<ResponseEntity<Void>> delete(@PathVariable String studyId) {
		return studyService.delete(studyId)
		                   .map(unused -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
		                   .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/join/{studyId}", method = RequestMethod.GET)
	public Mono<ResponseEntity<List<StudyJoin>>> joins(@PathVariable String studyId) {
		return studyJoinService.list(studyId)
		                       .map(joins -> ResponseEntity.status(HttpStatus.OK).body(joins))
		                       .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/join/{studyId}", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> join(@AuthenticationPrincipal Claims principal,
	                                       @PathVariable String studyId) {
		return studyJoinService.join(studyId, (String) principal.get("userId"))
		                       .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                       .onErrorReturn(ResponseEntity.internalServerError().build());
	}

}
