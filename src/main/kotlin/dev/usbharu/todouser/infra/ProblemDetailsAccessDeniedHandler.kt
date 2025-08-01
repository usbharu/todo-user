package dev.usbharu.todouser.infra

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*

@Component
class ProblemDetailsAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val status = HttpStatus.FORBIDDEN

        // ★ WWW-Authenticateヘッダーの生成
        val wwwAuthenticateDetails = StringJoiner(", ", "Bearer ", "")
        wwwAuthenticateDetails.add("realm=\"api\"")
        wwwAuthenticateDetails.add("error=\"insufficient_scope\"")
        wwwAuthenticateDetails.add(
            "error_description=\"The request requires higher privileges than provided by the access token.\""
        )

        // 本来は必要なスコープ情報をここに含めるのが望ましい
        // wwwAuthenticateDetails.add("scope=\"admin:read\"");
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticateDetails.toString())

        // ProblemDetailの生成
        val problemDetail = ProblemDetail.forStatusAndDetail(
            status,
            accessDeniedException.message
        )
        problemDetail.title = "Forbidden"
        problemDetail.setType(URI.create("https://example.com/probs/forbidden"))
        problemDetail.instance = URI.create(request.requestURI)

        // レスポンスの設定
        response.status = status.value()
        response.contentType = MediaType.APPLICATION_PROBLEM_JSON_VALUE
        objectMapper.writeValue(response.writer, problemDetail)
    }
}
