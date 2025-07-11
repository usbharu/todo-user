package dev.usbharu.todouser2.users

import java.util.UUID

@JvmInline
value class UserId(val id: UUID){
    companion object{
        val ZERO = UUID.fromString("0000000-0000-0000-0000-000000000000")
    }
}
