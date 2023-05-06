package com.grasstudy.study.event.scheme;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.common.event.EventMessage;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendEvent implements EventMessage {

	private Attend attend;
}
