package ru.zgrv.otrs.templates

enum class CallbackTemplates(val callback: String) {

    CREATE_PROFILE("create"),
    EDIT_PROFILE("edit"),
    CREATE_REQUEST("create_request"),
    CANCEL_REQUEST("cancel_request"),
    CANCEL_EDIT_PERSONAL_DATA("cancel_edit_personal_data"),

}