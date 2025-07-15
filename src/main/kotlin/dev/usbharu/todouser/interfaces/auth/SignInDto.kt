package dev.usbharu.todouser.interfaces.auth

/**
 * ログイン時に使用するパラメータ
 *
 * @property username ユーザー名
 * @property password パスワード(未ハッシュ化)
 */
data class SignInDto(
    val username: String,
    val password: String,
)