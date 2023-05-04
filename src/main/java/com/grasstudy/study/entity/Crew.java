package com.grasstudy.study.entity;

import com.grasstudy.common.entity.PersistableEntity;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Crew extends PersistableEntity {

	@Builder
	private Crew(String id, String studyId, String userId, Authority authority) {
		super(id);
		this.studyId = studyId;
		this.userId = userId;
		this.authority = authority;
	}

	private String studyId;
	private String userId;
	private Authority authority;

	public enum Authority {
		OWNER, SUB_OWNER, MEMBER
	}
}
