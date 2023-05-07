package com.grasstudy.study.event.scheme;

import com.grasstudy.study.entity.Study;
import com.grasstudy.common.event.EventMessage;
import lombok.*;


@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyCreateEvent implements EventMessage {

	private Study study;
}
