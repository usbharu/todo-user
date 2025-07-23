package dev.usbharu.todouser.domain.users

import dev.usbharu.todouser.domain.shared.ToDoUserException

class UserNameAlreadyUsedException : ToDoUserException {
    constructor(messageKey: String, args: Array<Any?>) : super(messageKey, args)
    constructor(message: String?, messageKey: String, args: Array<Any?>) : super(message, messageKey, args)
    constructor(message: String?, cause: Throwable?, messageKey: String, args: Array<Any?>) : super(
        message,
        cause,
        messageKey,
        args
    )

    constructor(cause: Throwable?, messageKey: String, args: Array<Any?>) : super(cause, messageKey, args)
    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
        messageKey: String,
        args: Array<Any?>
    ) : super(message, cause, enableSuppression, writableStackTrace, messageKey, args)
}