package dev.usbharu.todouser.application.users

import dev.usbharu.todouser.domain.users.UserId
import java.time.Instant

/**
 * 詳細なユーザー情報
 */
class UserDetail(username: String, userId: UserId, val createdAt: Instant) : User(username, userId) {
    companion object{
        fun from(user: dev.usbharu.todouser.domain.users.User): UserDetail {
            return UserDetail(user.username,user.userId,user.createdAt)
        }
    }
}