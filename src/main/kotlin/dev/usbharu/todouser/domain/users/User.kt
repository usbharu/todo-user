package dev.usbharu.todouser.domain.users

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

/**
 * ドメインエンティティ.
 *
 * そのままDBに保存されるエンティティでもある
 */
@Table("USERS")
class User(val username: String, @Id val userId: UserId, val password: String?, val createdAt: Instant) {
    init {
        require(username.length <= 255) {
            "username must be 255 characters long"
        }
    }
}
