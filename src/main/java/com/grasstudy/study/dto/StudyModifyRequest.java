package com.grasstudy.study.dto;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyMember;
import lombok.Data;

import java.util.List;

@Data
public class StudyModifyRequest extends StudyRequestBase {

	private List<StudyMember> members;

	public Study toEntity(String studyId) {
		return Study.builder()
		            .id(studyId)
		            .name(this.name)
		            .introduce(this.introduce)
		            .interestTags(this.interestTags)
		            .members(this.members)
		            .build();
	}
}
