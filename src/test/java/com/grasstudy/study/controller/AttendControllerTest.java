package com.grasstudy.study.controller;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.service.AttendService;
import com.grasstudy.study.test.WithMockUser;
import com.grasstudy.study.test.controller.ControllerTestBase;
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

import static org.mockito.ArgumentMatchers.*;

@WebFluxTest(controllers = AttendController.class)
class AttendControllerTest extends ControllerTestBase {

	@Autowired
	WebTestClient webTestClient;

	@SpyBean
	AttendController attendController;

	@MockBean
	AttendService attendService;

	@Test
	@WithMockUser
	void attendList() {
		List<Attend> mockReturn = List.of(MockData.attend(Attend.AttendState.WAIT));
		Mockito.when(attendService.list("test-id")).thenReturn(Mono.just(mockReturn));
		webTestClient.get()
		             .uri("/attend/test-id")
		             .exchange()
		             .expectStatus()
		             .isOk();
		Mockito.verify(attendController).attendList(eq("test-id"));
	}

	@Test
	@WithMockUser
	void attend() {
		Mockito.when(attendService.attend("test-id", "mock-user"))
		       .thenReturn(Mono.just(MockData.attend(Attend.AttendState.WAIT)));

		webTestClient.post()
		             .uri("/attend/test-id")
		             .exchange()
		             .expectStatus()
		             .isCreated();
		Mockito.verify(attendController).attend(any(Claims.class), eq("test-id"));
	}

	@Test
	@WithMockUser
	void manage() {
		Attend mockAttend = MockData.attend(Attend.AttendState.ACCEPT);
		Mockito.when(attendService.manage(any(Attend.class), anyString())).thenReturn(Mono.just(mockAttend));
		webTestClient.put()
		             .uri("/attend/test-id")
		             .contentType(MediaType.APPLICATION_JSON)
					 .bodyValue("{\n" +
							 "  \"userId\": \"member-id\",\n" +
							 "  \"studyId\": \"test-study-id\",\n" +
							 "  \"state\": \"ACCEPT\"\n" +
							 "}")
		             .exchange()
		             .expectStatus()
		             .isOk();
	}
}