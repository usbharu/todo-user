package dev.usbharu.todouser.application.users

import dev.usbharu.todouser.domain.users.UserId
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * 詳細なユーザー情報
 */
@Schema(description = "ユーザーの詳細な情報\nログインしているユーザー自身の情報")
class UserDetail(username: String, userId: UserId, val createdAt: Instant) : User(username, userId) {
    companion object {
        fun from(user: dev.usbharu.todouser.domain.users.User): UserDetail {
            return UserDetail(user.username, user.userId, user.createdAt)
        }
    }
}
