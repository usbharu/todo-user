package dev.usbharu.todouser2.domain.users

interface UserIdGenerator {
    suspend fun gen(): UserId
}