package ru.zgrv.otrs.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.zgrv.otrs.models.Person

@Repository
interface PersonRepository: JpaRepository<Person, Long> {
    fun findPersonByChatId(chatId: String): Person?
}