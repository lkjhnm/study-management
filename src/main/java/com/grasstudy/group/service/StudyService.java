package com.grasstudy.group.service;

import com.grasstudy.group.entity.Study;
import com.grasstudy.group.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepository repository;

	public void create(Study study) {
		this.repository.save(study);
	}
}
