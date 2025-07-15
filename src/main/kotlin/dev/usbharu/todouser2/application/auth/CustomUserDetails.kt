package dev.usbharu.todouser2.application.auth

import dev.usbharu.todouser2.domain.users.UserId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Spring Security用のログインユーザー情報
 */
class CustomUserDetails(
    private val username: String, val userId: UserId,private val password: String?,
    val provider: AuthenticateProvider = AuthenticateProvider.LOCAL,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return emptyList()
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String {
        return username
    }
}