package com.grasstudy.study.dto;

import com.grasstudy.study.entity.Study;
import lombok.Data;

@Data
public class StudyCreateParam extends StudyParamBase {

	public Study toEntity() {
		return Study.builder()
		            .id(null)
		            .name(this.name)
		            .introduce(this.introduce)
		            .interestTags(this.interestTags)
		            .build();
	}
}
