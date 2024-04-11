package ru.zgrv.otrs.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class EmailConfig {

    @Bean
    fun getJavaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587

        mailSender.username = "divit.otrs@gmail.com"
        mailSender.password = "fdsheqhstgraxqsg"

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp";
        props["mail.smtp.auth"] = "true";
        props["mail.smtp.starttls.enable"] = "true";
        props["mail.smtp.ssl.trust"] = "*";
        props["mail.debug"] = "true";

        return mailSender
    }
}