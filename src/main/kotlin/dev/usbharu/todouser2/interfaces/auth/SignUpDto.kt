package dev.usbharu.todouser2.interfaces.auth

/**
 * アカウント作成時に使用するパラメータ
 *
 * @property username ユーザー名
 * @property password パスワード(未ハッシュ化)
 */
data class SignUpDto(
    val username: String,
    val password: String,
)