package dev.usbharu.todouser.interfaces.api.user

import dev.usbharu.todouser.application.users.UserDetail
import dev.usbharu.todouser.domain.users.UserId
import dev.usbharu.todouser.domain.users.UserRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

/**
 * userの情報を返すAPI
 *
 * /api/v1/users/iでログイン中のユーザーの除法を取得できる
 */
@RestController
@RequestMapping("/api/v1/users")
class UsersController(private val userRepository: UserRepository) {
    @GetMapping("i")
    suspend fun getI(@AuthenticationPrincipal jwt: Jwt): UserDetail {
        val user = userRepository.findById(UserId.fromString(jwt.subject)).getOrNull()
            ?: throw IllegalArgumentException("User not found")
        return UserDetail.Companion.from(user)
    }
}