package com.grasstudy.study.service;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.attend.event.AttendEventPublisher;
import com.grasstudy.attend.event.scheme.AttendRequestEvent;
import com.grasstudy.study.repository.AttendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AttendService {

	private final AttendRepository attendRepository;
	private final AttendEventPublisher attendEventPublisher;
	private final StudyService studyService;

	public Mono<List<Attend>> list(String studyId) {
		return attendRepository.findAllById(Mono.just(studyId))
		                       .collectList();
	}

	public Mono<Attend> attend(String studyId, String userId) {
		return studyService.exist(studyId)
		                   .filter(exist -> exist)
		                   .switchIfEmpty(Mono.error(NoSuchElementException::new))
		                   .map(unused -> Attend.builder()
		                                          .studyId(studyId).userId(userId)
		                                          .state(Attend.AttendState.WAIT)
		                                          .build())
		                   .flatMap(attendRepository::save)
		                   .doOnNext(this::publishAttendEvent);
	}


	public Mono<Attend> manage(Attend attend, String userId) {

		// find user authority
		// update attend
		// if (approved) : add crews
		// publish event
		return attendRepository.save(attend);
	}

	private void publishAttendEvent(Attend attend) {
		this.attendEventPublisher.publish(AttendRequestEvent.builder()
		                                                    .attend(attend)
		                                                    .build());
	}
}
