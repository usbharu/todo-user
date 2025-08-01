package dev.usbharu.todouser.interfaces

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
@RestControllerAdvice
class GlobalControllerAdvice(private val messageSource: MessageSource) {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        webRequest: WebRequest
    ): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            messageSource.getMessage("error.validation.global.validationFailed", arrayOf(), webRequest.locale)
        ).apply {
            title = "Validation Failed"
            val errorDetails = ex.bindingResult.fieldErrors.map {
                ValidationErrorDetail(
                    field = it.field,
                    message = it.defaultMessage,
                    rejectedValue = it.rejectedValue?.toString()
                )
            }
            setProperty("errors", errorDetails)
        }
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.message)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ProblemDetail {
        logger.warn("Unhandled exception {} {}", ex::class.simpleName, ex.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalControllerAdvice::class.java)
    }
}

data class ValidationErrorDetail(
    val field: String,
    val message: String?,
    val rejectedValue: String?
)
