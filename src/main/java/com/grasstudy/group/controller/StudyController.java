package com.grasstudy.group.controller;

import com.grasstudy.group.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class StudyController {

	private final StudyGroupService groupService;

	@RequestMapping(value = "/group", method = RequestMethod.POST)
	public Mono<Void> create() {
		return Mono.empty();
	}
}
