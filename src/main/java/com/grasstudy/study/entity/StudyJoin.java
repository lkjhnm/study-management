package com.grasstudy.study.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyJoin extends PersistableEntity {

	@Builder
	private StudyJoin(String id, String studyId, String userId, JoinState state) {
		super(id);
		this.studyId = studyId;
		this.userId = userId;
		this.state = state;
	}

	private String studyId;
	private String userId;
	private JoinState state;

	public enum JoinState {
		WAIT, REJECT, ACCEPT
	}
}
