package com.grasstudy.study;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public interface TxStepVerifier extends StepVerifier {

	static <T> FirstStep<T> create(Mono<T> publisher) {
		return StepVerifier.create(publisher.as(RxTxTestSupporter::withRollback));
	}

	static <T> FirstStep<T> create(Flux<T> publisher) {
		return StepVerifier.create(publisher.as(RxTxTestSupporter::withRollback));
	}
}
