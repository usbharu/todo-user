package dev.usbharu.todouser.domain.users

import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun checkCreateUser(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw UserNameAlreadyUsedException("error.user.usernameAlreadyUsed", arrayOf(username))
        }
    }
}