package ru.zgrv.otrs.configs

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bot")
class BotProperties(val name: String, val token: String)