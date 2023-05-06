package com.grasstudy.study.dto;

import com.grasstudy.attend.entity.Attend;
import lombok.Data;

@Data
public class AttendManageParam {

	private String userId;
	private String studyId;
	private Attend.AttendState state;

	public Attend toEntity(String attendId) {
		return Attend.builder()
		             .id(attendId)
		             .userId(this.userId)
		             .studyId(this.studyId)
		             .state(this.state)
		             .build();
	}
}
