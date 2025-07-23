package dev.usbharu.todouser.utils

import dev.usbharu.todouser.domain.shared.ToDoUserException
import org.springframework.context.MessageSource
import java.util.Locale

object ExceptionUtils {
    fun buildLocalizedMessage(exception: ToDoUserException, locale: Locale, messageSource: MessageSource): String {
        if (exception.messageKey.isEmpty()) {
            return ""
        }
        val args: Array<Any> = arrayOf(exception.args.map {
            if (it is ToDoUserException) {
                messageSource.getMessage(it.messageKey, it.args, locale)
            } else {
                it
            }
        })
        return messageSource.getMessage(exception.messageKey, args, locale)
    }
}