package dev.usbharu.todouser2.application.auth

import dev.usbharu.todouser2.UserIdGenerator
import dev.usbharu.todouser2.application.users.UserDetail
import dev.usbharu.todouser2.domain.users.User
import dev.usbharu.todouser2.domain.users.UserRepository
import dev.usbharu.todouser2.interfaces.auth.SignUpDto
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SignUpService(
    private val userIdGenerator: UserIdGenerator,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate
) {
    suspend fun signUp(dto: SignUpDto): UserDetail {

        if (userRepository.findByUsername(username = dto.username) != null) {
            throw IllegalArgumentException("username already exists")
        }
        val user = User(dto.username, userIdGenerator.gen(), passwordEncoder.encode(dto.password), Instant.now())
        jdbcAggregateTemplate.insert(user)
        return UserDetail.from(user)
    }
}