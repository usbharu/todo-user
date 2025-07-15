package dev.usbharu.todouser2.domain.users

import org.springframework.data.repository.CrudRepository

/**
 * [User]のリポジトリ.
 *
 * Spring Data JDBCによって実装は自動生成される
 */
interface UserRepository : CrudRepository<User, UserId> {
    fun findByUsername(username: String): User?
}