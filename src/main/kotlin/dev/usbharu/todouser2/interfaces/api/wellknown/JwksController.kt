package dev.usbharu.todouser2.interfaces.api.wellknown

import dev.usbharu.todouser2.application.jwk.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping(".well-known")
class JwksController(private val jwtService: JwtService) {
    @GetMapping("jwks.json")
    suspend fun jwks(): ResponseEntity<Map<String?, Any?>?> {
        return ResponseEntity.ok(jwtService.jwks().toPublicJWKSet().toJSONObject())
    }
}