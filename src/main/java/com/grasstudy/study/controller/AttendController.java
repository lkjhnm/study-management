package com.grasstudy.study.controller;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.dto.AttendManageParam;
import com.grasstudy.study.service.AttendService;
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
@RequestMapping(value = "/attend")
@RequiredArgsConstructor
public class AttendController {

	private final AttendService attendService;

	@RequestMapping(value = "/{studyId}", method = RequestMethod.GET)
	public Mono<ResponseEntity<List<Attend>>> attendList(@PathVariable String studyId) {
		return attendService.list(studyId)
		                    .map(attendList -> ResponseEntity.status(HttpStatus.OK).body(attendList))
		                    .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/{studyId}", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> attend(@AuthenticationPrincipal Claims principal,
	                                         @PathVariable String studyId) {
		return attendService.attend(studyId, (String) principal.get("userId"))
		                    .map(unused -> ResponseEntity.status(HttpStatus.CREATED).<Void>build())
		                    .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/{attendId}", method = RequestMethod.PUT)
	public Mono<ResponseEntity<Void>> manage(@AuthenticationPrincipal Claims principal,
	                                         @PathVariable String attendId,
	                                         @RequestBody AttendManageParam manageParam) {
		return attendService.manage(manageParam.toEntity(attendId), (String) principal.get("userId"))
		                    .map(unused -> ResponseEntity.status(HttpStatus.OK).<Void>build())
		                    .onErrorReturn(ResponseEntity.internalServerError().build());
	}

}
