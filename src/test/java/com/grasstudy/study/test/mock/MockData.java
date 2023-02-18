package com.grasstudy.study.test.mock;

import com.grasstudy.study.entity.Study;
import com.grasstudy.study.entity.StudyJoin;
import com.grasstudy.study.entity.StudyMember;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	public static StudyMember studyMember(String id, String studyId, StudyMember.Authority authority) {
		return StudyMember.builder()
		                  .id(id)
		                  .studyId(studyId)
		                  .authority(authority)
		                  .userId("test-member")
		                  .build();
	}

	public static StudyMember studyMember(String studyId, StudyMember.Authority authority) {
		return StudyMember.builder()
		                  .studyId(studyId)
		                  .authority(authority)
		                  .userId("test-member")
		                  .build();
	}

	public static StudyJoin studyJoin() {
		return StudyJoin.builder()
		                .studyId("test-study-id")
		                .userId("test-user-id")
		                .state(StudyJoin.JoinState.WAIT)
		                .build();
	}
}
