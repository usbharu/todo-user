package dev.usbharu.todouser2

import dev.usbharu.todouser2.users.UserId
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TodoUser2Application

fun main(args: Array<String>) {
    runApplication<TodoUser2Application>(*args)
}
