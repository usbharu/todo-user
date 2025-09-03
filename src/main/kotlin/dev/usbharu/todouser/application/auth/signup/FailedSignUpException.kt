package dev.usbharu.todouser.application.auth.signup

import dev.usbharu.todouser.domain.shared.ToDoUserException

class FailedSignUpException : ToDoUserException {
    constructor(cause: Throwable?, args: Array<Any?>) : super(cause, "error.application.failedSignUp", args)
}
