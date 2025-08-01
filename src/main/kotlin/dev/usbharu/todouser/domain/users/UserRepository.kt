package dev.usbharu.todouser.domain.users

import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * [User]のリポジトリ.
 *
 * Spring Data JDBCによって実装は自動生成される
 */
interface UserRepository : CrudRepository<User, UUID> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}
