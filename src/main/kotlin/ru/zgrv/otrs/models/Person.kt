package ru.zgrv.otrs.models

import jakarta.persistence.*

@Entity
@Table(name = "people")
class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "chat_id")
    lateinit var chatId: String

    @Column(name = "phone")
    lateinit var phone: String

    @Column(name = "email")
    lateinit var email: String

    @Column(name = "company")
    lateinit var company: String
}