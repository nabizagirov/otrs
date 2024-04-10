package ru.zgrv.otrs.services

import org.springframework.stereotype.Service
import ru.zgrv.otrs.bot.Session
import ru.zgrv.otrs.repositories.PersonRepository
import ru.zgrv.otrs.repositories.SessionRepository

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val personRepository: PersonRepository,
) {

    fun getSession(chatId: String): Session {
        val session = sessionRepository.getOrCreateSession(chatId)
        val person = personRepository.findPersonByChatId(chatId)
        person?.let { session.updatePerson(it) }
        return session
    }

}