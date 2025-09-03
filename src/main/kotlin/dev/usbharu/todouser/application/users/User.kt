package dev.usbharu.todouser.application.users

import dev.usbharu.todouser.domain.users.UserId
import io.swagger.v3.oas.annotations.media.Schema

/**
 * ユーザー情報
 */
@Schema(description = "ログイン中のユーザー以外のユーザーの情報")
open class User(val username: String, val userId: UserId)
