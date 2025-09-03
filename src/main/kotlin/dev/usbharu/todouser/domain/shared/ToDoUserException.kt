package dev.usbharu.todouser.domain.shared

abstract class ToDoUserException : RuntimeException {
    val messageKey: String
    val args: Array<Any?>

    constructor(messageKey: String, args: Array<Any?>) : super() {
        this.messageKey = messageKey
        this.args = args
    }

    constructor(message: String?, messageKey: String, args: Array<Any?>) : super(message) {
        this.messageKey = messageKey
        this.args = args
    }

    constructor(message: String?, cause: Throwable?, messageKey: String, args: Array<Any?>) : super(message, cause) {
        this.messageKey = messageKey
        this.args = args
    }

    constructor(cause: Throwable?, messageKey: String, args: Array<Any?>) : super(cause) {
        this.messageKey = messageKey
        this.args = args
    }

    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
        messageKey: String,
        args: Array<Any?>
    ) : super(message, cause, enableSuppression, writableStackTrace) {
        this.messageKey = messageKey
        this.args = args
    }
}
