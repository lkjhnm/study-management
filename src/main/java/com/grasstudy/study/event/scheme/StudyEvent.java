package com.grasstudy.study.event.scheme;

import com.grasstudy.study.entity.Study;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@ToString
public class StudyEvent {

	private String action;
	private Study study;

	public enum ActionType {
		CREATE,
	}

	public static class StudyEventBuilder {
		private String action;

		public StudyEventBuilder action(ActionType actionType) {
			this.action = actionType.name();
			return this;
		}
	}
}
