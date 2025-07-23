package dev.usbharu.todouser.interfaces.auth

import dev.usbharu.todouser.application.auth.SignInService
import dev.usbharu.todouser.application.auth.signup.SignUpService
import dev.usbharu.todouser.application.users.UserDetail
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
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
    @Operation(
        summary = "ユーザー作成",
        tags = ["user"],
        responses = [ApiResponse(
            responseCode = "200",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserDetail::class),
                examples = [ExampleObject(
                    name = "ユーザー作成成功", value = """{
  "username": "f",
  "userId": "4f130f04-becf-4470-98bf-6c1c565f1ab4",
  "createdAt": "2025-07-23T17:06:27.690405800Z"
}"""
                )]
            )],
        ), ApiResponse(
            responseCode = "400",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "ユーザー名がすでに使用されている", value = """{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "ユーザーの作成に失敗しました：[ユーザー名fはすでに使用されています]",
  "instance": "/auth/v1/sign_up"
}"""
                )]
            )],
        )]
    )    @PostMapping(
        "/sign_up", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun signUpJson(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "作成するユーザーオブジェクト", required = true
        ) @RequestBody dto: SignUpDto
    ): UserDetail {
        return signUpService.signUp(dto)
    }


    /**
     * Formでユーザー生成
     */
    @Hidden
    @PostMapping(
        "/sign_up",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun signUpForm(@ModelAttribute dto: SignUpDto): ResponseEntity<UserDetail?> {
        return ResponseEntity.ok(signUpService.signUp(dto))
    }

    /**
     * JSONでログイン
     */
    @PostMapping(
        "/sign_in", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun signInJson(@RequestBody dto: SignInDto): ResponseEntity<Map<String, String>> {
        val signIn = signInService.signIn(dto.username, dto.password)
        return ResponseEntity.ok(mapOf("token" to signIn))
    }

    /**
     * Formでログイン
     */
    @Hidden
    @PostMapping(
        "/sign_in",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun signInForm(@ModelAttribute dto: SignInDto): ResponseEntity<Map<String, String>> {
        val signIn = signInService.signIn(dto.username, dto.password)
        return ResponseEntity.ok(mapOf("token" to signIn))
    }
}