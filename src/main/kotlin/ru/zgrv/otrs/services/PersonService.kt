package ru.zgrv.otrs.services

import org.springframework.stereotype.Service
import ru.zgrv.otrs.dto.PersonDTO
import ru.zgrv.otrs.models.Person
import ru.zgrv.otrs.repositories.PersonRepository
import ru.zgrv.otrs.utils.mapPersonDTOToPerson

@Service
class PersonService(private val personRepository: PersonRepository) {

    fun personExists(chatId: String): Boolean {
        return personRepository.findPersonByChatId(chatId) != null
    }

    fun save(person: PersonDTO): Person {
        return if (!personExists(person.chatId.toString())) personRepository.save(mapPersonDTOToPerson(person))
        else updatePerson(person)
    }

    fun getPerson(chatId: String): Person? = personRepository.findPersonByChatId(chatId)

    fun updatePerson(person: PersonDTO): Person {
        return personRepository.save(mapPersonDTOToPerson(person).apply {
            id = personRepository.findPersonByChatId(chatId)?.id ?: 0
        })
    }

}