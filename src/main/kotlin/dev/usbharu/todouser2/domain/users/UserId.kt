package dev.usbharu.todouser2.domain.users

import java.util.*

/**
 * ユーザーID
 */

@JvmInline
value class UserId(val id: UUID) {
    companion object {
        val ZERO = UUID.fromString("0000000-0000-0000-0000-000000000000")
    }
}