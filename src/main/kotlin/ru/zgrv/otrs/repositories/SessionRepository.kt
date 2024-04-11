package ru.zgrv.otrs.repositories

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository
import ru.zgrv.otrs.bot.Session

@Repository
class SessionRepository {
    private val sessions: MutableMap<String, Session> = mutableMapOf()

    fun getOrCreateSession(chatId: String): Session = sessions.getOrPut(chatId) { Session() }

    fun remove(chatId: String) = sessions.remove(chatId)

    @Scheduled(fixedDelay = 60000)
    private fun checkExpiredSessions() {
        sessions.forEach {
            val minutes = (System.currentTimeMillis() - it.value.lastActiveTime) / 60000
            if (minutes >= 10) remove(it.key)
        }
    }
}