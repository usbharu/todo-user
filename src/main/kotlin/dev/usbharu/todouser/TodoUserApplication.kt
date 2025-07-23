package dev.usbharu.todouser

import dev.usbharu.todouser.domain.users.UserId
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints

@OpenAPIDefinition(
    info = Info(
        title = "todo-user",
        version = "1.0.0",
        description = "TodoアプリバックエンドのUserと認証に関するAPI"
    )
)
@SpringBootApplication
@ConfigurationPropertiesScan
@ImportRuntimeHints(TodoUserApplication.Hints::class)
class TodoUserApplication {
    class Hints : RuntimeHintsRegistrar {
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            hints.reflection().registerTypeIfPresent(classLoader, UserId::class.java.name) {
                it.withMembers(*MemberCategory.entries.toTypedArray())
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TodoUserApplication>(*args)
}
