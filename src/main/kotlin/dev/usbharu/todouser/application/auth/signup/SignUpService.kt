package dev.usbharu.todouser.application.auth.signup

import dev.usbharu.todouser.application.users.UserDetail
import dev.usbharu.todouser.domain.shared.ToDoUserException
import dev.usbharu.todouser.domain.users.User
import dev.usbharu.todouser.domain.users.UserIdGenerator
import dev.usbharu.todouser.domain.users.UserService
import dev.usbharu.todouser.interfaces.auth.SignUpDto
import org.slf4j.LoggerFactory
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

/**
 * アカウント作成のアプリケーションサービス
 */
@Service
class SignUpService(
    private val userIdGenerator: UserIdGenerator,
    private val passwordEncoder: PasswordEncoder,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
    private val userService: UserService,
) {
    @Transactional()
    fun signUp(dto: SignUpDto): UserDetail {
        val user = try {
            userService.checkCreateUser(username = dto.username)
            val user = User(dto.username, userIdGenerator.gen(), passwordEncoder.encode(dto.password), Instant.now())
            jdbcAggregateTemplate.insert(user)
        } catch (ex: ToDoUserException) {
            throw FailedSignUpException(ex, args = arrayOf(ex))
        } catch (ex: RuntimeException) {
            throw SignUpProcessingException(ex)
        }
        logger.info("User ${dto.username} created.")
        return UserDetail.from(user)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SignUpService::class.java)
    }
}
