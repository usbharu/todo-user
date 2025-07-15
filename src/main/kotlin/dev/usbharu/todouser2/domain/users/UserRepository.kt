package dev.usbharu.todouser2.domain.users

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, UserId> {
    fun findByUsername(username: String): User?
}