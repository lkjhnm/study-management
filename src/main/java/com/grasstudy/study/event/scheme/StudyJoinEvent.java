package com.grasstudy.study.event.scheme;

import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.event.EventMessage;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyJoinEvent implements EventMessage {

	private StudyJoin studyJoin;
}
