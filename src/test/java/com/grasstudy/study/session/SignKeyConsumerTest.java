package com.grasstudy.study.session;

import com.grasstudy.common.session.PkiBasedJwtValidator;
import com.grasstudy.common.session.PkiBasedValidator;
import com.grasstudy.common.session.event.scheme.SigningKeyCreateEvent;
import com.grasstudy.study.test.mock.MockData;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.KeyPair;
import java.security.PublicKey;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@Import({PkiBasedJwtValidator.class, TestChannelBinderConfiguration.class, SessionEventConfiguration.class})
public class SignKeyConsumerTest {

	@Autowired
	InputDestination inputDestination;

	@Autowired
	PkiBasedValidator<Claims> pkiBasedValidator;

	@Test
	void sign_key_create_consume() {
		KeyPair signingKey = MockData.pairA;
		String kid = "test";
		PublicKey publicKey = signingKey.getPublic();
		SigningKeyCreateEvent event = SigningKeyCreateEvent.builder()
		                                                   .kid(kid)
		                                                   .algorithm(publicKey.getAlgorithm())
		                                                   .publicKey(publicKey.getEncoded())
		                                                   .build();
		Message<?> message = new GenericMessage<>(event);
		inputDestination.send(message, "keyCreateEventSubscriber-in-0");
		Assertions.assertThat(pkiBasedValidator.getPublicKey(kid)).isNotNull();
	}
}
