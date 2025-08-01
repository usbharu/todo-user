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
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
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
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDetail::class),
                        examples = [
                            ExampleObject(
                                name = "ユーザー作成成功",
                                value = """{
           "username": "f",
           "userId": "4f130f04-becf-4470-98bf-6c1c565f1ab4",
            "createdAt": "2025-07-23T17:06:27.690405800Z"
          }"""
                            )
                        ]
                    )
                ],
            ), ApiResponse(
                responseCode = "400",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class),
                        examples = [
                            ExampleObject(
                                name = "ユーザー名がすでに使用されている",
                                value = """{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "ユーザーの作成に失敗しました：[ユーザー名fはすでに使用されています]",
  "instance": "/auth/v1/sign_up"
}"""
                            ), ExampleObject(
                                name = "入力内容にエラーがある",
                                value = """
                            {
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "入力内容にエラーがあります",
  "instance": "/auth/v1/sign_up",
  "errors": [
    {
      "field": "username",
      "message": "ユーザー名に半角英数字以外が使用されています",
      "rejectedValue": "あ"
    }
  ]
}
                        """
                            ), ExampleObject(
                                name = "入力内容に複数エラーがある",
                                value = """
                            {
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "入力内容にエラーがあります",
  "instance": "/auth/v1/sign_up",
  "errors": [
    {
      "field": "password",
      "message": "パスワードは必須です",
      "rejectedValue": ""
    },
    {
      "field": "username",
      "message": "ユーザー名に半角英数字以外が使用されています",
      "rejectedValue": "あ"
    }
  ]
}
                        """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    @PostMapping(
        "/sign_up",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun signUpJson(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "作成するユーザーオブジェクト",
            required = true,
            content = [
                Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = SignUpDto::class),
                    examples = [
                        ExampleObject(
                            name = "期待されるリクエストボディ",
                            value = """{
                    "username": "testuser1234",
                    "password": "veeeeeeeeeeeeeryStrongPassword"
                    }"""
                        )
                    ]
                )
            ]
        ) @RequestBody @Validated dto: SignUpDto
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
    suspend fun signUpForm(@ModelAttribute @Validated dto: SignUpDto): ResponseEntity<UserDetail?> {
        return ResponseEntity.ok(signUpService.signUp(dto))
    }

    /**
     * JSONでログイン
     */
    @Operation(
        summary = "ログイン",
        tags = ["user"],
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Token::class),
                        examples = [
                            ExampleObject(
                                name = "ログイン成功",
                                value = """
                            {
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ1c2VyIiwic3ViIjoiNThlZDk0NzItZWE2MS00ZmZkLTk1YzYtODRiNzc3Nzc0MzUwIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.uqs4KgPYEgJYwSOPnxJNwXh9EIpJ3AKYrPgy0PxLw71V7zoOwLxdXHjDxTKwBhmsnqk18yVpIoOQbWG3Eoaj43z5skkVdtNIHT9cT_23upjtzB0dyT08M8G5TqNIrM96EfChStOxIeaDzaENLLfpucLFDoqDmuKBWqGmGYRgjEt_tXTSB66GJ0Ldrw1N1fLLXitdHGxVJwOhweqsfLLidLPCgfl5c5JbkFevRUWRORMKZ7oGUylXFvonNX8Ohwm4qK82ah_JqRRlllozN74ZrzJQdy4XX88oA9hphcOh7gc4i1iLMsTvK7kOzEQOyuUaSyctJZ61gLeWLhptYqPo4Q"
}
                        """
                            )
                        ]
                    )
                ]
            ), ApiResponse(
                responseCode = "400",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class),
                        examples = [
                            ExampleObject(
                                name = "入力内容にエラーがある",
                                value = """
                            {
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "入力内容にエラーがあります",
  "instance": "/auth/v1/sign_up",
  "errors": [
    {
      "field": "username",
      "message": "ユーザー名に半角英数字以外が使用されています",
      "rejectedValue": "あ"
    }
  ]
}
                        """
                            ), ExampleObject(
                                name = "入力内容に複数エラーがある",
                                value = """
                            {
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "入力内容にエラーがあります",
  "instance": "/auth/v1/sign_up",
  "errors": [
    {
      "field": "password",
      "message": "パスワードは必須です",
      "rejectedValue": ""
    },
    {
      "field": "username",
      "message": "ユーザー名に半角英数字以外が使用されています",
      "rejectedValue": "あ"
    }
  ]
}
                        """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class),
                        examples = [
                            ExampleObject(
                                name = "ユーザー名かパスワードが正しくない",
                                value = """
                        {
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "ユーザ名かパスワードが正しくありません",
  "instance": "/auth/v1/sign_in"
}
                    """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    @PostMapping(
        "/sign_in",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun signInJson(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "ログインするユーザーオブジェクト",
            required = true,
            content = [
                Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = SignInDto::class),
                    examples = [
                        ExampleObject(
                            name = "期待されるリクエストボディ",
                            value = """{
                    "username": "testuser1234",
                    "password": "veeeeeeeeeeeeeryStrongPassword"
                    }
                    """
                        )
                    ]
                )
            ]
        ) @RequestBody @Validated dto: SignInDto
    ): ResponseEntity<Token> {
        val signIn = signInService.signIn(dto.username, dto.password)
        return ResponseEntity.ok(Token(signIn))
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
