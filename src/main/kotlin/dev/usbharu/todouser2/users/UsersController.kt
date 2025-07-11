package dev.usbharu.todouser2.users

import com.nimbusds.jwt.JWT
import dev.usbharu.todouser2.domain.users.UserRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UsersController(private val userRepository: UserRepository) {
    @GetMapping("i")
    suspend fun getI(@AuthenticationPrincipal jwt: Jwt): UserDetail {
        val user = userRepository.findByUsername(jwt.subject) ?: throw IllegalArgumentException("User not found")
        return UserDetail.from(user)
    }
}