package dev.usbharu.todouser2

import dev.usbharu.todouser2.domain.users.UserId

interface UserIdGenerator {
    suspend fun gen(): UserId
}