package dev.usbharu.todouser.application.auth

import dev.usbharu.todouser.application.users.UserDetail
import dev.usbharu.todouser.domain.users.User
import dev.usbharu.todouser.domain.users.UserIdGenerator
import dev.usbharu.todouser.domain.users.UserRepository
import dev.usbharu.todouser.interfaces.auth.SignUpDto
import org.slf4j.LoggerFactory
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

/**
 * アカウント作成のアプリケーションサービス
 */
@Service
class SignUpService(
    private val userIdGenerator: UserIdGenerator,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate
) {
    suspend fun signUp(dto: SignUpDto): UserDetail {
        if (userRepository.findByUsername(username = dto.username) != null) {
            logger.info("User ${dto.username} already exists.")
            throw IllegalArgumentException("username already exists")
        }
        val user = User(dto.username, userIdGenerator.gen(), passwordEncoder.encode(dto.password), Instant.now())
        logger.info("User ${dto.username} created.")
        jdbcAggregateTemplate.insert(user)
        return UserDetail.from(user)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SignUpService::class.java)
    }
}