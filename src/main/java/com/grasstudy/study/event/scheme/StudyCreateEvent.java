package com.grasstudy.study.event.scheme;

import com.grasstudy.study.entity.Study;
import lombok.*;


@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyCreateEvent {

	private Study study;
}
