package dev.usbharu.todouser2.application.users

import dev.usbharu.todouser2.domain.users.UserId
import java.time.Instant

class UserDetail(username: String, userId: UserId, val createdAt: Instant) : User(username, userId) {
    companion object{
        fun from(user: dev.usbharu.todouser2.domain.users.User):UserDetail{
            return UserDetail(user.username,user.userId,user.createdAt)
        }
    }
}