package dev.usbharu.todouser.infra

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

class MdcXRequestIdFilter(val requestIdKey: String, val requestIdHeaderName: String) : OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            MDC.put(
                requestIdKey,
                if (request.getHeader(requestIdHeaderName)?.isNotBlank() == true) {
                    request.getHeader(requestIdHeaderName)
                } else {
                    UUID.randomUUID().toString()
                }
            )
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove(requestIdKey)
        }
    }
}