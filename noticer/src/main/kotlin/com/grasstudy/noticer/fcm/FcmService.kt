package com.grasstudy.noticer.fcm

import com.google.auth.oauth2.GoogleCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.FileInputStream

@Service
class FcmService {

	@Value("\${grasstudy.google.credentials}")
	private lateinit var googleCredentials: String

	private fun auth(): String {
		val credentials = GoogleCredentials.fromStream(FileInputStream(googleCredentials))
			.createScoped("https://www.googleapis.com/auth/firebase")
		credentials.refreshIfExpired();
		return credentials.accessToken.tokenValue
	}

	fun push(message: String) {

	}
}