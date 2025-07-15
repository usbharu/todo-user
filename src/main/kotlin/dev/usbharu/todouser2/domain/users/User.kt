package dev.usbharu.todouser2.domain.users

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("USERS")
class User(val username: String, @Id val userId: UserId, val password: String?, val createdAt: Instant)
