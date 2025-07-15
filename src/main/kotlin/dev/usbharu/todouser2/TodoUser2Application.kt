package dev.usbharu.todouser2

import dev.usbharu.todouser2.domain.users.UserId
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints

@SpringBootApplication
@ConfigurationPropertiesScan
@ImportRuntimeHints(TodoUser2Application.Hints::class)
class TodoUser2Application{
    class Hints : RuntimeHintsRegistrar {
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            hints.reflection().registerTypeIfPresent(classLoader, UserId::class.java.name) {
                it.withMembers(*MemberCategory.entries.toTypedArray())
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TodoUser2Application>(*args)
}
