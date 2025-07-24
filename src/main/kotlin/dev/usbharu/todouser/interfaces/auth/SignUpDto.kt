package dev.usbharu.todouser.interfaces.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * アカウント作成時に使用するパラメータ
 *
 * @property username ユーザー名
 * @property password パスワード(未ハッシュ化)
 */
data class SignUpDto(
    @field:NotBlank(message = "{error.validation.user.username.notblank}")
    @field:Size(max = 255, message = "{error.validation.user.username.size}")
    @field:Pattern(regexp = "[a-zA-Z0-9]+", message = "{error.validation.user.username.pattern}")
    val username: String,
    @field:NotBlank(message = "{error.validation.user.password.notblank}")
    val password: String,
)