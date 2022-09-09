package com.grasstudy.group.service;

import com.grasstudy.group.entity.Study;
import com.grasstudy.group.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

	private final StudyGroupRepository repository;

	public void create(Study study) {
		this.repository.save(study);
	}
}
