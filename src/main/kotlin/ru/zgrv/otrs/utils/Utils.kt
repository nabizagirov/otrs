package ru.zgrv.otrs.utils

import ru.zgrv.otrs.bot.Session
import ru.zgrv.otrs.dto.PersonDTO
import ru.zgrv.otrs.models.Person
import ru.zgrv.otrs.templates.TextTemplates


fun replaceIllegalCharacters(string: String): String {
    return string.replace("+", "\\+").replace("-", "\\-").replace("*", "\\*")
        .replace("_", "\\_").replace("#", "\\#").replace(".", "\\.")
}

fun displayProfileText(person: Person): String =
    """
        📑 *Ваши данные*
        
        • ФИО: ${replaceIllegalCharacters(person.name)}
        • Номер телефона: ${replaceIllegalCharacters(person.phone)}
        • Почта: ${replaceIllegalCharacters(person.email)}
        • Компания: ${replaceIllegalCharacters(person.company)}
    """.trimIndent()

fun fullRequestText(session: Session): String =
    """
        Данные заявителя
        • ФИО: ${session.person.name}
        • Номер телефона: ${session.person.phone}
        • Почта: ${session.person.email}
        • Компания: ${session.person.company}
        ------------------------------
        Текст заявки:
        ${session.dataFiller.problem}
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