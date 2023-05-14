package com.grasstudy.noticer.event.attend.scheme

data class AttendEvent(val studyId: String, val userId: String, val state: AttendState) {
	override fun toString(): String {
		return "AttendEvent(studyId='$studyId', userId='$userId', state=$state)"
	}
}
