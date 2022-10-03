package com.grasstudy.study.entity;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Study extends PersistableEntity {

	@Builder
	private Study(String id, String name, List<String> interestTags, String introduce, List<StudyMember> members) {
		super(id);
		this.name = name;
		this.interestTags = interestTags;
		this.introduce = introduce;
		this.members = members;
	}

	private String name;
	private List<String> interestTags;
	private String introduce;

	@Transient
	private List<StudyMember> members;
}


