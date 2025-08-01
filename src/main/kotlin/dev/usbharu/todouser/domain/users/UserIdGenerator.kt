package dev.usbharu.todouser.domain.users

interface UserIdGenerator {
    fun gen(): UserId
}
