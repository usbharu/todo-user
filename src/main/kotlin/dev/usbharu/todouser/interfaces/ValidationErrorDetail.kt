package dev.usbharu.todouser.interfaces

data class ValidationErrorDetail(
    val field: String,
    val message: String?,
    val rejectedValue: String?
)
