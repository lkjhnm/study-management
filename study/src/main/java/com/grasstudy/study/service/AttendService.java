package com.grasstudy.study.service;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.entity.Crew;
import com.grasstudy.study.event.AttendEventPublisher;
import com.grasstudy.study.event.scheme.AttendEvent;
import com.grasstudy.study.repository.AttendRepository;
import com.grasstudy.study.repository.CrewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class AttendService {

	private final AttendRepository attendRepository;
	private final AttendEventPublisher attendEventPublisher;
	private final StudyService studyService;
	private final CrewRepository crewRepository;

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


	public Mono<Attend> manage(Attend attend, String ownerId) {
		return Mono.just(attend)
		           .filter(this.checkAuthority(ownerId))
		           .switchIfEmpty(Mono.error(RuntimeException::new)) // todo: change unuathorize exception
		           .map(this::update)
		           .doOnNext(this::publishAttendEvent);
	}

	private Predicate<Attend> checkAuthority(String ownerId) {
		return attend -> this.crewRepository.findByStudyIdAndUserId(attend.getStudyId(), ownerId)
		                                    .map(Crew::getAuthority)
		                                    .map(Crew.Authority.OWNER::equals)
		                                    .blockOptional().orElse(false);
	}

	private Attend update(Attend attend) {
		return this.attendRepository
				.save(attend)
				.filter(v -> v.getState() == Attend.AttendState.ACCEPT)
				.doOnNext(saved -> crewRepository.save(Crew.builder()
				                                           .studyId(saved.getStudyId())
				                                           .userId(saved.getUserId())
				                                           .authority(Crew.Authority.MEMBER).build()))
				.defaultIfEmpty(attend)
				.block();
	}

	private void publishAttendEvent(Attend attend) {
		this.attendEventPublisher.publish(AttendEvent.builder()
		                                             .attend(attend)
		                                             .build());
	}
}
