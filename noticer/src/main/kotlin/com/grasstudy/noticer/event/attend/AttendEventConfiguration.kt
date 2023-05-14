package com.grasstudy.noticer.event.attend

import com.grasstudy.noticer.event.attend.scheme.AttendEvent
import com.grasstudy.noticer.event.attend.scheme.AttendState
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AttendEventConfiguration {

	val logger: Logger = LoggerFactory.getLogger(javaClass)

	@Bean
	fun attendEventSwitch(): Subscriber<AttendEvent> {
		return object : Subscriber<AttendEvent> {
			override fun onSubscribe(s: Subscription) {
				logger.info("Initialize complete {}", s)
				s.request(Long.MAX_VALUE)
				// does need to control flow?
			}

			override fun onNext(t: AttendEvent) {
				logger.info("{}", t)
				when (t.state) {
					AttendState.WAIT -> processWait(t)
					AttendState.ACCEPT -> processAccept(t)
					AttendState.REJECT -> processReject(t)
				}
			}

			override fun onError(t: Throwable) {
				logger.error("{}", t)
				processError()
				// todo: record
			}

			override fun onComplete() {
				// is this error?
				processError()
			}
		}
	}

	private fun processWait(attendEvent: AttendEvent) {
		logger.info("{}", attendEvent)
		//todo: 스터디장에게 알림 보내기
	}

	private fun processAccept(attendEvent: AttendEvent) {
		logger.info("{}", attendEvent)
		//todo: 기존 & 신규 스터디원에게 알림 보내기
	}

	private fun processReject(attendEvent: AttendEvent) {
		//todo: 유저에게 거절 알림 보내기
	}

	private fun processError() {
		//todo: do action when error
	}
}