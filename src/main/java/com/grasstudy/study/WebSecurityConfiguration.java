package com.grasstudy.study;

import com.grasstudy.common.security.reactive.JwtAuthenticationConverter;
import com.grasstudy.common.security.reactive.JwtAuthenticationManager;
import com.grasstudy.common.session.PkiBasedJwtValidator;
import com.grasstudy.common.session.PkiBasedValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
	                                                  WebFilter jwtAuthenticationFilter) {
		return http
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.authorizeExchange().anyExchange().authenticated().and()
				.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.build();
	}

	@Bean
	WebFilter jwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
		AuthenticationWebFilter webFilter = new AuthenticationWebFilter(authenticationManager);
		webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.anyExchange());
		webFilter.setServerAuthenticationConverter(new JwtAuthenticationConverter());
		webFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(authenticationEntryPoint()));
		return webFilter;
	}

	@Bean
	public ReactiveAuthenticationManager authenticationManager(PkiBasedValidator<Claims> jwtValidator) {
		return new JwtAuthenticationManager(jwtValidator);
	}

	@Bean
	public PkiBasedValidator<Claims> jwtValidator() {
		return new PkiBasedJwtValidator();
	}

	private ServerAuthenticationEntryPoint authenticationEntryPoint() {
		return (exchange, ex) -> Mono.fromRunnable(() -> {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
		});
	}
}
