package com.grasstudy.study.test.mock;

import com.grasstudy.study.entity.Study;
import com.grasstudy.attend.entity.Attend;
import com.grasstudy.study.entity.Crew;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MockData {

	public static KeyPair pairA = Keys.keyPairFor(SignatureAlgorithm.ES256);
	public static KeyPair pairB = Keys.keyPairFor(SignatureAlgorithm.ES256);

	public static String jwtToken(String kid, KeyPair keyPair) {
		return Jwts.builder()
		           .setHeaderParam("kid", kid)
		           .signWith(keyPair.getPrivate())
		           .setClaims(Map.of("userId", "mock-user"))
		           .setExpiration(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault())
		                                                 .toInstant()))
		           .setIssuedAt(new Date())
		           .compact();
	}

	public static Study study(String id) {
		return Study.builder()
		            .id(id)
		            .name("test-study")
		            .interestTags(List.of("java", "msa"))
		            .build();
	}

	public static Study study() {
		return Study.builder()
		            .name("test-study")
		            .interestTags(List.of("java", "msa"))
		            .build();
	}

	public static Crew managedCrew(String studyId, Crew.Authority authority) {
		UUID uuid = UUID.randomUUID();
		return managedCrew(uuid.toString(), studyId, authority, UUID.randomUUID().toString());
	}

	public static Crew managedCrew(String id, String studyId, Crew.Authority authority, String userId) {
		return Crew.builder()
		           .id(id)
		           .studyId(studyId)
		           .authority(authority)
		           .userId(userId)
		           .build();
	}

	public static Crew transientCrew(String studyId, Crew.Authority authority, String userId) {
		return managedCrew(null, studyId, authority, userId);
	}

	public static Attend attend(Attend.AttendState state) {
		return Attend.builder()
		             .studyId("test-study-id")
		             .userId("test-user-id")
		             .state(state)
		             .build();
	}
}
