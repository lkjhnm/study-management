package com.grasstudy.attend.entity;

import com.grasstudy.common.entity.PersistableEntity;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Attend extends PersistableEntity {

	@Builder
	private Attend(String id, String studyId, String userId, AttendState state) {
		super(id);
		this.studyId = studyId;
		this.userId = userId;
		this.state = state;
	}

	private String studyId;
	private String userId;
	private AttendState state;

	public enum AttendState {
		WAIT, REJECT, ACCEPT
	}
}
