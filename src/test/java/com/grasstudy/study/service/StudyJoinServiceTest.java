package com.grasstudy.study.service;

import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.event.StudyEventPublisher;
import com.grasstudy.study.mock.MockData;
import com.grasstudy.study.repository.StudyJoinRepository;
import com.grasstudy.study.repository.StudyRepository;
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
public class StudyJoinServiceTest {

	@InjectMocks
	StudyJoinService studyJoinService;

	@Mock
	StudyJoinRepository studyJoinRepository;

	@Mock
	StudyRepository studyRepository;

	@Mock
	StudyEventPublisher studyEventPublisher;

	@Test
	public void join_success() {
		StudyJoin mockStudyJoin = MockData.studyJoin();
		String mockStudyId = "test-study-id";
		Mockito.when(studyRepository.findById(mockStudyId)).thenReturn(Mono.just(MockData.study(mockStudyId)));
		Mockito.when(studyJoinRepository.save(mockStudyJoin)).thenReturn(Mono.just(mockStudyJoin));

		studyJoinService.join(mockStudyJoin.getStudyId(), mockStudyJoin.getUserId())
		                .as(StepVerifier::create)
		                .expectNext(mockStudyJoin)
		                .verifyComplete();
		Mockito.verify(studyEventPublisher, times(1)).publish(any());
	}

	@Test
	public void join_fail() {
		String mockStudyId = "test-study-id";
		Mockito.when(studyRepository.findById(mockStudyId)).thenReturn(Mono.empty());

		studyJoinService.join(mockStudyId, "user-id")
		                .as(StepVerifier::create)
		                .expectError(NoSuchElementException.class)
                        .verify();

		Mockito.verify(studyJoinRepository, times(0)).save(any());
		Mockito.verify(studyEventPublisher, times(0)).publish(any());
	}
}
