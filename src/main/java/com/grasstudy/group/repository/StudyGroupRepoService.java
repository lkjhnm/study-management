package com.grasstudy.group.repository;

import com.grasstudy.group.entity.Study;
import com.grasstudy.group.entity.StudyMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyGroupRepoService {

	private final StudyGroupRepository groupRepository;
	private final StudyMemberRepository memberRepository;

	public Mono<Study> fetchOne(Long studyId) {
		Mono<Study> studyGroup = groupRepository.findById(studyId);
		Mono<List<StudyMember>> members = memberRepository.findAllByStudyGroupId(studyId)
		                                                            .collectList();
		return Mono.zip(studyGroup, members)
				.map(t ->{
					Study study = t.getT1();
					study.setMembers(t.getT2());
					return study;
				});
	}
}
