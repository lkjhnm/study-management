package com.grasstudy.study.service;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.repository.StudyRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepoService repository;

	public void create(Study study) {
		this.repository.create(study);
	}
}
