package com.grasstudy.study.mock;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyMember;

import java.util.List;

public class MockData {

	public static Study mockStudy(String id) {
		return Study.builder()
		            .id(id)
		            .name("test-study")
		            .interestTags(List.of("java", "msa"))
		            .build();
	}

	public static Study newStudy() {
		return Study.builder()
		            .name("test-study")
		            .interestTags(List.of("java", "msa"))
		            .build();
	}

	public static StudyMember studyMember(String id, String studyId, StudyMember.Authority authority) {
		return StudyMember.builder()
		                  .id(id)
		                  .studyId(studyId)
		                  .authority(authority)
		                  .userId("test-member")
		                  .build();
	}

	public static StudyMember newStudyMember(String studyId, StudyMember.Authority authority) {
		return StudyMember.builder()
		                  .studyId(studyId)
		                  .authority(authority)
		                  .userId("test-member")
		                  .build();
	}
}
