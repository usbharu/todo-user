package dev.usbharu.todouser2.interfaces.api.wellknown

import dev.usbharu.todouser2.application.jwk.JwkService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Json Web Key Setの公開エンドポイント
 */
@RestController()
@RequestMapping(".well-known")
class JwksController(private val jwkService: JwkService) {
    @GetMapping("jwks.json")
    suspend fun jwks(): ResponseEntity<Map<String?, Any?>?> {
        return ResponseEntity.ok(jwkService.jwks().toPublicJWKSet().toJSONObject())
    }
}