package ru.zgrv.otrs.bot

import ru.zgrv.otrs.dto.PersonDTO
import ru.zgrv.otrs.models.Person

class Session {

    lateinit var person: Person
    var lastActiveTime: Long = System.currentTimeMillis()
    var stage = SessionStage.MENU
    val dataFiller = DataFiller()



    fun updatePerson(person: Person) {
        this.person = person
    }

    class DataFiller {
        private val stages = DataFillerStage.entries.toList()
        var problem = ""
        var stage = stages.first()
        var dto = PersonDTO()


        fun next() {
            if (stage == stages.last()) return
            stage = stages[stages.indexOf(stage) + 1]
        }

        fun filled() = stage == stages.last()

        fun reset() {
            stage = stages.first()
        }

        fun enterData(data: String) {
            when (stage) {
                DataFillerStage.NAME -> dto.name = data
                DataFillerStage.PHONE -> dto.phone = data
                DataFillerStage.EMAIL -> dto.email = data
                DataFillerStage.COMPANY -> dto.company = data
                DataFillerStage.END -> return
            }
        }

        fun setChatId(chatId: String) {
            dto.chatId = chatId
        }

        enum class DataFillerStage {
            NAME, PHONE, EMAIL, COMPANY, END
        }
    }


}
