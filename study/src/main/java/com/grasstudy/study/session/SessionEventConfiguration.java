package com.grasstudy.study.session;

import com.grasstudy.common.session.PkiBasedValidator;
import com.grasstudy.common.session.event.ReactorSigningKeySubscriber;
import com.grasstudy.common.session.event.scheme.SigningKeyCreateEvent;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class SessionEventConfiguration {

	@Bean
	public Consumer<Flux<SigningKeyCreateEvent>> keyCreateEventSubscriber(PkiBasedValidator<Claims> jwtValidator) {
		return new ReactorSigningKeySubscriber().subscriber(jwtValidator);
	}
}
