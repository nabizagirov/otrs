package ru.zgrv.otrs.repositories

import org.springframework.stereotype.Repository
import ru.zgrv.otrs.bot.Session

@Repository
class SessionRepository {
    private val sessions: MutableMap<String, Session> = mutableMapOf()

    fun getOrCreateSession(chatId: String): Session = sessions.getOrPut(chatId) { Session() }

    fun remove(chatId: String) = sessions.remove(chatId)
}