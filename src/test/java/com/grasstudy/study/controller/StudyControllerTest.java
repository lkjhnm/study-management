package com.grasstudy.study.controller;

import com.grasstudy.study.WithMockUser;
import com.grasstudy.study.dto.StudyCreateRequest;
import com.grasstudy.study.dto.StudyModifyRequest;
import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.service.StudyJoinService;
import com.grasstudy.study.service.StudyService;
import com.grasstudy.study.test.mock.MockData;
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

import java.util.List;

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

	@MockBean
	StudyJoinService studyJoinService;

	@Test
	void unauthorized_request() {
		webTestClient.post()
		             .uri("/study")
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
		             .uri("/study")
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue("{\n" +
				             "  \"name\": \"test\",\n" +
				             "  \"introduce\" : \"test-introduce\"}")
		             .exchange()
		             .expectStatus()
		             .isCreated();
		Mockito.verify(studyController).create(any(Claims.class), any(StudyCreateRequest.class));
	}

	@Test
	@WithMockUser
	void modify() {
		Mockito.when(studyService.modify(any(Study.class))).thenReturn(Mono.just(MockData.study()));
		webTestClient.put()
		             .uri("/study/test-id")
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue("{\n" +
				             "  \"name\": \"test\",\n" +
				             "  \"introduce\" : \"test-introduce\"}")
		             .exchange()
		             .expectStatus()
		             .isNoContent();
		Mockito.verify(studyController).modify(eq("test-id"), any(StudyModifyRequest.class));
	}

	@Test
	@WithMockUser
	void delete() {
		Mockito.when(studyService.delete(eq("test-id"))).thenReturn(Mono.just("test-id"));
		webTestClient.delete()
		             .uri("/study/test-id")
		             .exchange()
		             .expectStatus()
		             .isNoContent();
		Mockito.verify(studyController).delete(eq("test-id"));
	}

	@Test
	@WithMockUser
	void joins() {
		List<StudyJoin> mockReturn = List.of(MockData.studyJoin());
		Mockito.when(studyJoinService.list("test-id")).thenReturn(Mono.just(mockReturn));
		webTestClient.get()
		             .uri("/study/join/test-id")
				.exchange()
				.expectStatus()
				.isOk();
		Mockito.verify(studyController).joins(eq("test-id"));
	}

	@Test
	@WithMockUser
	void join() {
		Mockito.when(studyJoinService.join("test-id", "mock-user"))
		       .thenReturn(Mono.just(MockData.studyJoin()));

		webTestClient.post()
		             .uri("/study/join/test-id")
		             .exchange()
		             .expectStatus()
		             .isCreated();
		Mockito.verify(studyController).join(any(Claims.class), eq("test-id"));
	}
}
