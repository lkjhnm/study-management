package com.grasstudy.study.session;

import com.grasstudy.study.WebSecurityConfiguration;
import com.grasstudy.study.controller.StudyController;
import com.grasstudy.study.service.StudyJoinService;
import com.grasstudy.study.service.StudyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

@WebFluxTest(controllers = StudyController.class)
@Import(WebSecurityConfiguration.class)
public class WebSecurityTest {

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	StudyService studyService;

	@MockBean
	StudyJoinService studyJoinService;

	@Test
	void unauthorized_empty_token() {
		webTestClient.post()
		             .uri("/study", Map.of("name", "test",
				             "introduce","test-introduce",
				             "userId", "test-user"
		             ))
		             .exchange()
		             .expectStatus()
		             .isUnauthorized();
	}

}
