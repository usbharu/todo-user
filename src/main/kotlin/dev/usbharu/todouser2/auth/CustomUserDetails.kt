package dev.usbharu.todouser2.auth

import dev.usbharu.todouser2.users.UserId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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