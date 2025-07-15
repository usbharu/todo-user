package dev.usbharu.todouser.interfaces.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * 検証用の認証ページ表示用コントローラ
 *
 */
@Controller
class AuthViewController {
    @GetMapping("/sign_in")
    fun signIn(): String = "sign_in"

    @GetMapping("/sign_up")
    fun signUp(): String = "sign_up"
}