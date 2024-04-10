package ru.zgrv.otrs.utils

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class MessageBuilder {

    companion object {

        fun build(text: String, chatId: String, keyboard: List<List<InlineKeyboardButton>> = emptyList()): SendMessage {
            return SendMessage().apply {
                enableMarkdownV2(true)
                this.text = text
                this.chatId = chatId
                replyMarkup = InlineKeyboardMarkup().apply { this.keyboard = keyboard }
            }
        }
    }
}