package dev.usbharu.todouser.infra

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*


@Component
class ProblemDetailsAuthenticationEntryPoint(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val status = HttpStatus.UNAUTHORIZED
        var errorMessage: String? = authException.message
        val problemDetail = ProblemDetail.forStatusAndDetail(status, errorMessage)


        // ★ WWW-Authenticateヘッダーの生成
        if (authException is OAuth2AuthenticationException) {
            val error: OAuth2Error = authException.error


            // ProblemDetailの情報をOAuth2Errorの内容で上書き
            errorMessage = error.description
            problemDetail.detail = errorMessage
            problemDetail.title = error.errorCode

            // WWW-Authenticateヘッダーを組み立てる
            val wwwAuthenticateDetails = StringJoiner(", ", "Bearer ", "")
            wwwAuthenticateDetails.add("realm=\"api\"") // realmは任意
            if (error.errorCode != null) {
                wwwAuthenticateDetails.add("error=\"" + error.errorCode + "\"")
            }
            if (error.description != null) {
                wwwAuthenticateDetails.add("error_description=\"" + error.description + "\"")
            }
            response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticateDetails.toString())
        } else {
            problemDetail.title = "Authentication Failed"
        }

        problemDetail.instance = URI.create(request.requestURI)


        // レスポンスの設定
        response.status = status.value()
        response.contentType = MediaType.APPLICATION_PROBLEM_JSON_VALUE
        objectMapper.writeValue(response.writer, problemDetail)
    }
}