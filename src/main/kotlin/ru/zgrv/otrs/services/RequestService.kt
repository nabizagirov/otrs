package ru.zgrv.otrs.services

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.zgrv.otrs.bot.Session
import ru.zgrv.otrs.bot.SessionStage
import ru.zgrv.otrs.bot.TelegramBot
import ru.zgrv.otrs.models.Person
import ru.zgrv.otrs.templates.CallbackTemplates
import ru.zgrv.otrs.templates.KeyboardTemplates
import ru.zgrv.otrs.templates.TextTemplates
import ru.zgrv.otrs.utils.MessageBuilder
import ru.zgrv.otrs.utils.displayProfileText
import ru.zgrv.otrs.utils.textFormByStage

@Service
class RequestService(
    private val sessionService: SessionService,
    private val personService: PersonService,
) {

    fun handleUpdate(bot: TelegramBot, update: Update) {
        if (update.hasMessage()) return handleMessage(bot, update.message)
        if (update.hasCallbackQuery()) return handleCallbackQuery(bot, update.callbackQuery)
    }

    private fun handleMessage(bot: TelegramBot, message: Message) {
        val chatId = message.chatId.toString()
        val text = message.text
        val session = sessionService.getSession(chatId)

        // Обработка нового пользователя
        if (isNewUser(chatId) && session.stage != SessionStage.CHANGING_PERSONAL_DATA) {
            newUserMessage(bot, chatId)
            return
        }

        // Обработка ввода данных пользователя
        if (session.stage == SessionStage.CHANGING_PERSONAL_DATA) {
            session.dataFiller.enterData(text)
            session.dataFiller.next()

            /* Проверка, что анкета заполнена,
               выход из стадии заполнения */
            if (session.dataFiller.filled()) {
                savePerson(session, chatId)
                session.updatePerson(personService.getPerson(chatId)!!)
                session.stage = SessionStage.MENU
                displayProfileMessage(bot, chatId, session.person)
                return
            }

            bot.execute(MessageBuilder.build(textFormByStage(session), chatId))
            return
        }

        //
        if(session.stage == SessionStage.MAKING_REQUEST) {
            session.stage = SessionStage.SENDING_REQUEST
            session.dataFiller.problem = text
            bot.execute(MessageBuilder.build(TextTemplates.BEFORE_REQUEST_SEND.text, chatId))

            // TODO("ОТПРАВКА СООБЩЕНИЙ")
            bot.execute(MessageBuilder.build(TextTemplates.AFTER_REQUEST_SEND_SUCCESS.text, chatId))
            session.stage = SessionStage.MENU

            return
        }

        displayProfileMessage(bot, chatId, session.person)
    }


    private fun handleCallbackQuery(bot: TelegramBot, callbackQuery: CallbackQuery) {
        val chatId = callbackQuery.message.chatId.toString()
        val data = callbackQuery.data
        val session = sessionService.getSession(chatId)

        // Обработка нажатия кнопки заполнения анкеты
        if (isNewUser(chatId) && data == CallbackTemplates.CREATE_PROFILE.callback) {
            session.stage = SessionStage.CHANGING_PERSONAL_DATA
            session.dataFiller.reset()
            session.dataFiller.setChatId(chatId)
            bot.execute(MessageBuilder.build(textFormByStage(session), chatId))
        }

        // Переход из стадии МЕНЮ в стадию создания заявки
        if (session.stage == SessionStage.MENU && data == CallbackTemplates.CREATE_REQUEST.callback) {
            session.stage = SessionStage.MAKING_REQUEST
            bot.execute(
                MessageBuilder.build(
                    TextTemplates.MAKE_REQUEST.text, chatId,
                    keyboard = KeyboardTemplates.CANCEL_REQUEST.keyboard
                )
            )

            return
        }

        // Редактирование персональных данных
        if (session.stage == SessionStage.MENU && data == CallbackTemplates.EDIT_PROFILE.callback) {
            session.stage = SessionStage.CHANGING_PERSONAL_DATA
            session.dataFiller.reset()
            bot.execute(
                MessageBuilder.build(
                    TextTemplates.EDIT_PERSONAL_DATA.text, chatId,
                    keyboard = KeyboardTemplates.EDIT_PROFILE.keyboard
                )
            )
            bot.execute(MessageBuilder.build(textFormByStage(session), chatId))

            return
        }

        // Отмена редактирования персональных данных
        if (session.stage == SessionStage.CHANGING_PERSONAL_DATA
            && data == CallbackTemplates.CANCEL_EDIT_PERSONAL_DATA.callback
        ) {
            session.stage = SessionStage.MENU
            session.updatePerson(personService.getPerson(chatId)!!)
            session.dataFiller.reset()
            bot.execute(MessageBuilder.build(TextTemplates.CANCELLED_EDIT_PERSONAL_DATA.text, chatId))
            displayProfileMessage(bot, chatId, session.person)

            return
        }


    }


    private fun newUserMessage(bot: TelegramBot, chatId: String) {
        bot.execute(
            MessageBuilder.build(TextTemplates.NEW_USER_ALERT.text, chatId)
        )

        bot.execute(
            MessageBuilder.build(
                TextTemplates.NEW_USER_START_BUTTON.text, chatId,
                keyboard = KeyboardTemplates.NEW_USER.keyboard
            )
        )
    }


    private fun displayProfileMessage(bot: TelegramBot, chatId: String, person: Person) {
        bot.execute(
            MessageBuilder.build(
                displayProfileText(person), chatId, keyboard = KeyboardTemplates.MENU.keyboard
            )
        )
    }

    private fun isNewUser(chatId: String): Boolean = !personService.personExists(chatId)

    private fun savePerson(session: Session, chatId: String) {
        personService.save(session.dataFiller.dto.apply { this.chatId = chatId })
    }
}