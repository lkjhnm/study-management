package com.grasstudy.study.event;

import com.grasstudy.common.session.PkiBasedValidator;
import com.grasstudy.common.session.event.scheme.SigningKeyCreateEvent;
import com.grasstudy.study.StudyApplication;
import com.grasstudy.study.test.mock.MockData;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;

import java.security.KeyPair;
import java.security.PublicKey;

@SpringBootTest
@ContextConfiguration(classes = {TestChannelBinderConfiguration.class, StudyApplication.class})
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
		inputDestination.send(message, "signkey-create-event");
		Assertions.assertThat(pkiBasedValidator.getPublicKey(kid)).isNotNull();
	}
}
