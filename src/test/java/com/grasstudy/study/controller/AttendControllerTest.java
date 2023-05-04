package com.grasstudy.study.controller;

import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.service.AttendService;
import com.grasstudy.study.test.WithMockUser;
import com.grasstudy.study.test.mock.MockData;
import com.grasstudy.study.test.controller.ControllerTestBase;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
		List<Attend> mockReturn = List.of(MockData.attend());
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
		       .thenReturn(Mono.just(MockData.attend()));

		webTestClient.post()
		             .uri("/attend/test-id")
		             .exchange()
		             .expectStatus()
		             .isCreated();
		Mockito.verify(attendController).attend(any(Claims.class), eq("test-id"));
	}
}