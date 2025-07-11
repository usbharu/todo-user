package dev.usbharu.todouser2.domain.users

import dev.usbharu.todouser2.users.UserId
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, UserId> {
    fun findByUsername(username: String): User?
}