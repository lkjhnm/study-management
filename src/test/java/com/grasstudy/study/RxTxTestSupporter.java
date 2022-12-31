package com.grasstudy.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RxTxTestSupporter {

	private static TransactionalOperator rxtx;

	@Autowired
	public RxTxTestSupporter(TransactionalOperator transactionalOperator) {
		rxtx = transactionalOperator;
	}

	static <T> Mono<T> withRollback(final Mono<T> publisher) {
		return rxtx.execute(tx -> {
			tx.setRollbackOnly();
			return publisher;
		}).next();
	}

	static <T> Flux<T> withRollback(final Flux<T> publisher) {
		return rxtx.execute(tx -> {
			tx.setRollbackOnly();
			return publisher;
		});
	}
}
