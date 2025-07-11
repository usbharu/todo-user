package dev.usbharu.todouser2.auth

import dev.usbharu.todouser2.users.UserDetail
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/v1")
class AuthController(private val signUpService: SignUpService, private val authService: AuthService) {
    @PostMapping("/sign_up", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun signUpJson(@RequestBody dto: SignUpDto): ResponseEntity<UserDetail> {
        return ResponseEntity.ok(signUpService.signUp(dto))
    }

    @PostMapping("/sign_up", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    suspend fun signUpForm(@ModelAttribute dto: SignUpDto): ResponseEntity<UserDetail?> {
        return ResponseEntity.ok(signUpService.signUp(dto))
    }

    @PostMapping("/sign_in", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun signInJson(@RequestBody dto: SignInDto): ResponseEntity<Map<String, String>> {
        val signIn = authService.signIn(dto.username, dto.password)
        return ResponseEntity.ok(mapOf("token" to signIn))
    }
}