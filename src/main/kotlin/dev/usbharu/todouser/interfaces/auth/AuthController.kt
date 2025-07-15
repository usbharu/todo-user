package dev.usbharu.todouser.interfaces.auth

import dev.usbharu.todouser.application.auth.SignInService
import dev.usbharu.todouser.application.auth.SignUpService
import dev.usbharu.todouser.application.users.UserDetail
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 認証APIのコントローラ
 */
@RestController
@RequestMapping("/auth/v1")
class AuthController(private val signUpService: SignUpService, private val signInService: SignInService) {

    /**
     * JSONでユーザー生成
     */
    @PostMapping("/sign_up", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun signUpJson(@RequestBody dto: SignUpDto): ResponseEntity<UserDetail> {
        return ResponseEntity.ok(signUpService.signUp(dto))
    }


    /**
     * Formでユーザー生成
     */
    @PostMapping("/sign_up", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    suspend fun signUpForm(@ModelAttribute dto: SignUpDto): ResponseEntity<UserDetail?> {
        return ResponseEntity.ok(signUpService.signUp(dto))
    }

    /**
     * JSONでログイン
     */
    @PostMapping("/sign_in", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun signInJson(@RequestBody dto: SignInDto): ResponseEntity<Map<String, String>> {
        val signIn = signInService.signIn(dto.username, dto.password)
        return ResponseEntity.ok(mapOf("token" to signIn))
    }

    /**
     * Formでログイン
     */
    @PostMapping("/sign_in", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    suspend fun signInForm(@ModelAttribute dto: SignInDto): ResponseEntity<Map<String, String>> {
        val signIn = signInService.signIn(dto.username, dto.password)
        return ResponseEntity.ok(mapOf("token" to signIn))
    }
}