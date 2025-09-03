package dev.usbharu.todouser.interfaces

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.*
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestControllerAdvice
class GlobalControllerAdvice(private val messageSource: MessageSource) : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        return ResponseEntity<ProblemDetail>.badRequest().body(handleMethodArgumentNotValidException(ex, request))
    }

    //    @ExceptionHandler(MethodArgumentNotValidException::class)
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

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalControllerAdvice::class.java)
    }
}
