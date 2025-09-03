package dev.usbharu.todouser.domain.users

import java.util.*

/**
 * ユーザーID
 */

@JvmInline
value class UserId(val id: UUID) {
    companion object {
        fun fromString(subject: String): UserId {
            return UserId(UUID.fromString(subject))
        }

        val ZERO = UUID.fromString("0000000-0000-0000-0000-000000000000")
    }
}
