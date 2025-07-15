package dev.usbharu.todouser2.infra

import dev.usbharu.todouser2.domain.users.UserId
import dev.usbharu.todouser2.domain.users.UserIdGenerator
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDUserIdGenerator : UserIdGenerator {
    override suspend fun gen(): UserId {
        return UserId(UUID.randomUUID())
    }
}