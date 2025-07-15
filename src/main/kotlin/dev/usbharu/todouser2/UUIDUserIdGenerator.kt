package dev.usbharu.todouser2

import dev.usbharu.todouser2.domain.users.UserId
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDUserIdGenerator : UserIdGenerator {
    override suspend fun gen(): UserId {
        return UserId(UUID.randomUUID())
    }
}