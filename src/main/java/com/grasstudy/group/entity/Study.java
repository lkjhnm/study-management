package com.grasstudy.group.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Study {

	@Id
	private Long id;
	private String name;
	private List<String> interestTags;
	private String introduce;

	@Transient
	private List<StudyMember> members;
}


