package dev.usbharu.todouser2

import dev.usbharu.todouser2.users.UserId
import org.springframework.stereotype.Component

@Component
class UUIDUserIdGenerator : UserIdGenerator {
    override suspend fun gen(): UserId {
        return UserId(java.util.UUID.randomUUID())
    }
}