package com.grasstudy.noticer.fcm

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@Import(FcmService::class)
@ExtendWith(SpringExtension::class)
@TestPropertySource(locations = ["classpath:application-test.properties"])
class FcmServiceTest {

	@SpyBean
	lateinit var fcmService: FcmService

	@Test
	fun push() {

	}
}