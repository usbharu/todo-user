package dev.usbharu.todouser2.application.auth

import dev.usbharu.todouser2.domain.users.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

/**
 * Spring Security用のログインユーザー取得サービス
 */
@Component
class MyUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        val user = userRepository.findByUsername(username) ?: return null
        if (user.password == null) {
            return null
        }
        return CustomUserDetails(user.username, user.userId, user.password)
    }

}