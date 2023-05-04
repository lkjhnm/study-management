package com.grasstudy.study.controller;

import com.grasstudy.study.dto.StudyCreateParam;
import com.grasstudy.study.dto.StudyModifyParam;
import com.grasstudy.study.entity.Study;
import com.grasstudy.study.test.WithMockUser;
import com.grasstudy.study.service.StudyService;
import com.grasstudy.study.test.mock.MockData;
import com.grasstudy.study.test.controller.ControllerTestBase;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(controllers = StudyController.class)
public class StudyControllerTest extends ControllerTestBase {

	@Autowired
	WebTestClient webTestClient;

	@SpyBean
	StudyController studyController;

	@MockBean
	StudyService studyService;

	static final String STUDY_API_BASE = "/study";

	@Test
	void unauthorized_request() {
		webTestClient.post()
		             .uri(STUDY_API_BASE)
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue("{\n" +
				             "  \"name\": \"test\",\n" +
				             "  \"introduce\" : \"test-introduce\"}")
		             .exchange()
		             .expectStatus()
		             .isUnauthorized();
	}

	@Test
	@WithMockUser
	void create() {
		Mockito.when(studyService.create(eq("mock-user"), any(Study.class)))
		       .thenReturn(Mono.just(MockData.study()));

		webTestClient.post()
		             .uri(STUDY_API_BASE)
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue("{\n" +
				             "  \"name\": \"test\",\n" +
				             "  \"introduce\" : \"test-introduce\"}")
		             .exchange()
		             .expectStatus()
		             .isCreated();
		Mockito.verify(studyController).create(any(Claims.class), any(StudyCreateParam.class));
	}

	@Test
	@WithMockUser
	void modify() {
		Mockito.when(studyService.modify(any(Study.class))).thenReturn(Mono.just(MockData.study()));
		webTestClient.put()
		             .uri(String.format("%s/test-id", STUDY_API_BASE))
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue("{\n" +
				             "  \"name\": \"test\",\n" +
				             "  \"introduce\" : \"test-introduce\"}")
		             .exchange()
		             .expectStatus()
		             .isNoContent();
		Mockito.verify(studyController).modify(eq("test-id"), any(StudyModifyParam.class));
	}

	@Test
	@WithMockUser
	void delete() {
		Mockito.when(studyService.delete(eq("test-id"))).thenReturn(Mono.just("test-id"));
		webTestClient.delete()
		             .uri(String.format("%s/test-id", STUDY_API_BASE))
		             .exchange()
		             .expectStatus()
		             .isNoContent();
		Mockito.verify(studyController).delete(eq("test-id"));
	}
}
