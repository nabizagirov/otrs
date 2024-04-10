package ru.zgrv.otrs.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.zgrv.otrs.configs.BotProperties
import ru.zgrv.otrs.services.RequestService

@Component
class TelegramBot(
    private val props: BotProperties,
    private val requestService: RequestService
) : TelegramLongPollingBot(props.token) {

    override fun getBotUsername(): String = props.name

    override fun onUpdateReceived(update: Update?) {
        update?.run { requestService.handleUpdate(this@TelegramBot, update) }

    }
}