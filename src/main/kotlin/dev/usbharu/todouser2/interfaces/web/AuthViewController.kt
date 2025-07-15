package dev.usbharu.todouser2.interfaces.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AuthViewController {
    @GetMapping("/sign_in")
    fun signIn(): String = "sign_in"

    @GetMapping("/sign_up")
    fun signUp(): String = "sign_up"
}