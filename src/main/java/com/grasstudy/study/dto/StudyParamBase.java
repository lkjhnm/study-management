package com.grasstudy.study.dto;

import lombok.Data;

import java.util.List;

@Data
abstract class StudyParamBase {
	protected String name;
	protected List<String> interestTags;
	protected String introduce;
}
