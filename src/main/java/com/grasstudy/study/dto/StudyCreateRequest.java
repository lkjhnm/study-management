package com.grasstudy.study.dto;

import com.grasstudy.study.entity.Study;
import lombok.Data;

@Data
public class StudyCreateRequest extends StudyRequestBase {

	public Study toEntity() {
		return Study.builder()
		            .id(null)
		            .name(this.name)
		            .introduce(this.introduce)
		            .interestTags(this.interestTags)
		            .build();
	}
}
