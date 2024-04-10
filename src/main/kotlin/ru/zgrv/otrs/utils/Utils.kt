package ru.zgrv.otrs.utils

import ru.zgrv.otrs.bot.Session
import ru.zgrv.otrs.dto.PersonDTO
import ru.zgrv.otrs.models.Person
import ru.zgrv.otrs.templates.TextTemplates

fun displayProfileText(person: Person): String =
    """
        📑 *Ваши данные*
        
        • ФИО: ${person.name}
        • Номер телефона: ${person.phone}
        • Почта: ${person.email}
        • Компания: ${person.company}
    """.trimIndent()


fun textFormByStage(session: Session): String = when (session.dataFiller.stage) {
    Session.DataFiller.DataFillerStage.NAME -> TextTemplates.ENTER_NAME.text
    Session.DataFiller.DataFillerStage.PHONE -> TextTemplates.ENTER_PHONE.text
    Session.DataFiller.DataFillerStage.EMAIL -> TextTemplates.ENTER_MAIL.text
    Session.DataFiller.DataFillerStage.COMPANY -> TextTemplates.ENTER_COMPANY.text
    Session.DataFiller.DataFillerStage.END -> ""
}

fun mapPersonToPersonDTO(person: Person): PersonDTO =
    PersonDTO().apply {
        name = person.name
        email = person.email
        phone = person.phone
        chatId = person.chatId
        company = person.company
    }

fun mapPersonDTOToPerson(person: PersonDTO): Person = person.let {
    Person().apply {
        name = person.name.toString()
        email = person.email.toString()
        phone = person.phone.toString()
        chatId = person.chatId.toString()
        company = person.company.toString()
    }
}