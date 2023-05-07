package com.grasstudy.study.dto;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.Crew;
import lombok.Data;

import java.util.List;

@Data
public class StudyModifyParam extends StudyParamBase {

	private List<Crew> crews;

	public Study toEntity(String studyId) {
		return Study.builder()
		            .id(studyId)
		            .name(this.name)
		            .introduce(this.introduce)
		            .interestTags(this.interestTags)
		            .crews(this.crews)
		            .build();
	}
}
