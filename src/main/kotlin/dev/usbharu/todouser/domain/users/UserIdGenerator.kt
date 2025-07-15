package dev.usbharu.todouser.domain.users

interface UserIdGenerator {
    suspend fun gen(): UserId
}