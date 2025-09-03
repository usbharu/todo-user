package dev.usbharu.todouser.application.auth

import dev.usbharu.todouser.domain.users.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

/**
 * Spring Security用のログインユーザー取得サービス
 */
@Component
class MyUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        if (user.password == null) {
            logger.debug("User {} has not password login.", username)
            throw UsernameNotFoundException(user.username)
        }
        return CustomUserDetails(user.username, user.userId, user.password)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MyUserDetailsService::class.java)
    }
}
