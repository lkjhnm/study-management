package com.grasstudy.study.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table
public class StudyMember {

	@Id
	private Long id;
	private Long studyId;
	private String userId;

	private Authority authority;

	public enum Authority {
		OWNER, SUB_OWNER, MEMBER
	}
}
