package com.grasstudy.study.mock;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.entity.StudyMember;

import java.util.List;

public class MockData {

	public static Study study(String id) {
		return Study.builder()
		            .id(id)
		            .name("test-study")
		            .interestTags(List.of("java", "msa"))
		            .build();
	}

	public static Study study() {
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

	public static StudyMember studyMember(String studyId, StudyMember.Authority authority) {
		return StudyMember.builder()
		                  .studyId(studyId)
		                  .authority(authority)
		                  .userId("test-member")
		                  .build();
	}

	public static StudyJoin studyJoin() {
		return StudyJoin.builder()
		                .studyId("test-study-id")
		                .userId("test-user-id")
		                .state(StudyJoin.JoinState.WAIT)
		                .build();
	}
}
