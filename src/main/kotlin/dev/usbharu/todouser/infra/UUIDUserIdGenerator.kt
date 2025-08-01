package dev.usbharu.todouser.infra

import dev.usbharu.todouser.domain.users.UserId
import dev.usbharu.todouser.domain.users.UserIdGenerator
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDUserIdGenerator : UserIdGenerator {
    override fun gen(): UserId {
        return UserId(UUID.randomUUID())
    }
}
