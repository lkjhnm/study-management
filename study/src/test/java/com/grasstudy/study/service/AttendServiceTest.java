package com.grasstudy.study.service;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.entity.Crew;
import com.grasstudy.study.event.AttendEventPublisher;
import com.grasstudy.study.event.scheme.AttendEvent;
import com.grasstudy.study.repository.AttendRepository;
import com.grasstudy.study.repository.CrewRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
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

	@Mock
	CrewRepository crewRepository;

	@Test
	public void attend_request_success() {
		Attend mockAttend = MockData.attend(Attend.AttendState.WAIT);
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
	public void attend_request_fail() {
		String mockStudyId = "test-study-id";
		Mockito.when(studyService.exist(mockStudyId)).thenReturn(Mono.just(false));

		attendService.attend(mockStudyId, "user-id")
		             .as(StepVerifier::create)
		             .expectError(NoSuchElementException.class)
		             .verify();

		Mockito.verify(attendRepository, times(0)).save(any());
		Mockito.verify(attendEventPublisher, times(0)).publish(any());
	}

	@Test
	public void attend_approve() {
		Attend mockAttend = MockData.attend(Attend.AttendState.ACCEPT);
		Crew mockCrewOwner = MockData.managedCrew("test-study-id", Crew.Authority.OWNER);
		Crew mockCrewMember = MockData.managedCrew("test-study-id", Crew.Authority.MEMBER);
		Mockito.when(crewRepository.findByStudyIdAndUserId(anyString(), anyString())).thenReturn(Mono.just(mockCrewOwner));
		Mockito.when(attendRepository.save(mockAttend)).thenReturn(Mono.just(mockAttend));
		Mockito.when(crewRepository.save(any(Crew.class))).thenReturn(Mono.just(mockCrewMember));

		attendService.manage(mockAttend, "owner-id")
		             .as(StepVerifier::create)
		             .expectNext(mockAttend)
		             .verifyComplete();

		Mockito.verify(crewRepository, times(1)).save(any(Crew.class));
		Mockito.verify(attendEventPublisher, times(1)).publish(any(AttendEvent.class));
	}

	@Test
	public void attend_reject() {
		Attend mockAttend = MockData.attend(Attend.AttendState.REJECT);
		Crew mockCrewOwner = MockData.managedCrew("test-study-id", Crew.Authority.OWNER);
		Mockito.when(crewRepository.findByStudyIdAndUserId(anyString(), anyString())).thenReturn(Mono.just(mockCrewOwner));
		Mockito.when(attendRepository.save(mockAttend)).thenReturn(Mono.just(mockAttend));

		attendService.manage(mockAttend, "owner-id")
		             .as(StepVerifier::create)
		             .expectNext(mockAttend)
		             .verifyComplete();

		Mockito.verify(crewRepository, times(0)).save(any(Crew.class));
		Mockito.verify(attendEventPublisher, times(1)).publish(any(AttendEvent.class));
	}

	@Test
	public void attend_unauthorized() {
		Attend mockAttend = MockData.attend(Attend.AttendState.ACCEPT);
		Crew unAuthCrew = MockData.managedCrew("test-study-id", Crew.Authority.MEMBER);
		Mockito.when(crewRepository.findByStudyIdAndUserId(anyString(), anyString())).thenReturn(Mono.just(unAuthCrew));

		attendService.manage(mockAttend, "owner-id")
		             .as(StepVerifier::create)
		             .expectError()
		             .verify();

		Mockito.verify(crewRepository, times(0)).save(any(Crew.class));
		Mockito.verify(attendEventPublisher, times(0)).publish(any(AttendEvent.class));
	}
}
