package com.grasstudy.study.service;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.attend.event.AttendEventPublisher;
import com.grasstudy.study.repository.AttendRepository;
import com.grasstudy.study.test.mock.MockData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class AttendServiceTest {

	@InjectMocks
	AttendService attendService;

	@Mock
	AttendRepository attendRepository;

	@Mock
	StudyService studyService;

	@Mock
	AttendEventPublisher attendEventPublisher;

	@Test
	public void attend_success() {
		Attend mockAttend = MockData.attend();
		String mockStudyId = "test-study-id";
		Mockito.when(studyService.exist(mockStudyId)).thenReturn(Mono.just(true));
		Mockito.when(attendRepository.save(mockAttend)).thenReturn(Mono.just(mockAttend));

		attendService.attend(mockAttend.getStudyId(), mockAttend.getUserId())
		             .as(StepVerifier::create)
		             .expectNext(mockAttend)
		             .verifyComplete();
		Mockito.verify(attendEventPublisher, times(1)).publish(any());
	}

	@Test
	public void attend_fail() {
		String mockStudyId = "test-study-id";
		Mockito.when(studyService.exist(mockStudyId)).thenReturn(Mono.just(false));

		attendService.attend(mockStudyId, "user-id")
		             .as(StepVerifier::create)
		             .expectError(NoSuchElementException.class)
		             .verify();

		Mockito.verify(attendRepository, times(0)).save(any());
		Mockito.verify(attendEventPublisher, times(0)).publish(any());
	}
}
