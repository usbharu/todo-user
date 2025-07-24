package dev.usbharu.todouser.interfaces.api.user

import dev.usbharu.todouser.application.users.UserDetail
import dev.usbharu.todouser.domain.users.UserId
import dev.usbharu.todouser.domain.users.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

/**
 * userの情報を返すAPI
 *
 * /api/v1/users/iでログイン中のユーザーの除法を取得できる
 */
@RestController
@RequestMapping("/api/v1/users")
class UsersController(private val userRepository: UserRepository) {
    @Operation(
        summary = "現在ログインしているユーザーの取得", tags = ["user"], responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = UserDetail::class),
                        examples = [
                            ExampleObject(
                                name = "ログイン中のユーザー取得成功",
                                value = """{
  "username": "ab",
  "userId": "77b714bc-f064-42ad-b01c-ad4a1d4bd87e",
  "createdAt": "2025-07-24T15:06:06.647007100Z"
}"""
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                headers = [
                    Header(
                        name = HttpHeaders.WWW_AUTHENTICATE,
                        description = "401となった原因",
                        examples = [
                            ExampleObject(
                                name = "トークンがJWTの形式じゃない",
                                value = "Bearer realm=\"api\", error=\"invalid_token\", error_description=\"An error occurred while attempting to decode the Jwt: Malformed token\""
                            )
                        ]
                    )
                ],
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class),
                        examples = [
                            ExampleObject(
                                name = "ログイン情報が間違っている",
                                value = """{
  "type": "about:blank",
  "title": "Authentication Failed",
  "status": 401,
  "detail": "Full authentication is required to access this resource",
  "instance": "/api/v1/users/i"
}"""
                            ),
                            ExampleObject(
                                name = "ログイントークンが不正",
                                value = """{
  "type": "about:blank",
  "title": "invalid_token",
  "status": 401,
  "detail": "An error occurred while attempting to decode the Jwt: Malformed token",
  "instance": "/api/v1/users/i"
}"""
                            ),
                            ExampleObject(
                                name = "トークンの署名が不正",
                                description = "トークンが古いなどの可能性がある",
                                value = """{
  "type": "about:blank",
  "title": "invalid_token",
  "status": 401,
  "detail": "An error occurred while attempting to decode the Jwt: Signed JWT rejected: Invalid signature",
  "instance": "/api/v1/users/i"
}"""
                            )
                        ]
                    )
                ]
            )
        ]
    )
    @SecurityRequirement(name = "jwt")
    @GetMapping("i", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getI(@AuthenticationPrincipal jwt: Jwt): UserDetail {
        val user = userRepository.findById(UserId.fromString(jwt.subject)).getOrNull()
            ?: throw IllegalArgumentException("User not found")
        return UserDetail.Companion.from(user)
    }
}