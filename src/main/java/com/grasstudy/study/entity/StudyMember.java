package com.grasstudy.study.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@Table
public class StudyMember implements Persistable<String> {

	@Id
	private String id;
	private String studyId;
	private String userId;

	@Override
	public boolean isNew() {
		boolean isNew = Objects.isNull(this.id);
		this.id = isNew ? UUID.randomUUID().toString() : this.id;
		return isNew;
	}

	private Authority authority;

	public enum Authority {
		OWNER, SUB_OWNER, MEMBER
	}
}
