package dev.usbharu.todouser.application.auth.signup

import dev.usbharu.todouser.domain.shared.ToDoUserException

class SignUpProcessingException : ToDoUserException {
    constructor(cause: Throwable?) : super(cause, "error.application.failedSignUpUnknown", arrayOf(cause))
}
