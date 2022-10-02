package com.grasstudy.study.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Table
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Study implements Persistable<String> {

	@Id
	private String id;
	private String name;
	private List<String> interestTags;
	private String introduce;

	@Override
	public boolean isNew() {
		boolean isNew = Objects.isNull(this.id);
		this.id = isNew ? UUID.randomUUID().toString() : this.id;
		return isNew;
	}

	@Transient
	private List<StudyMember> members;
}


