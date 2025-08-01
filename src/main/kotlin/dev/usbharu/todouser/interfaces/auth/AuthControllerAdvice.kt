package dev.usbharu.todouser.interfaces.auth

import dev.usbharu.todouser.application.auth.signup.FailedSignUpException
import dev.usbharu.todouser.utils.ExceptionUtils
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice("dev.usbharu.todouser.interfaces.auth")
class AuthControllerAdvice(private val messageSource: MessageSource) {

    @ExceptionHandler(FailedSignUpException::class)
    fun handleFailedSignUpException(exception: FailedSignUpException, webRequest: WebRequest): ProblemDetail {
        val message = ExceptionUtils.buildLocalizedMessage(exception, webRequest.locale, messageSource)

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message)
    }
}
