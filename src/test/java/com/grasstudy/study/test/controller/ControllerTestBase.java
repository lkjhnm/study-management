package com.grasstudy.study.test.controller;


import com.grasstudy.WebSecurityConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.test.context.TestExecutionListeners;

@Import(WebSecurityConfiguration.class)
@TestExecutionListeners(value = {ControllerTestExecutionListener.class},
		mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ControllerTestBase {

	@MockBean
	ReactiveAuthenticationManager authenticationManager;
}
