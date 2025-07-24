package dev.usbharu.todouser.interfaces.api.wellknown

import dev.usbharu.todouser.application.jwk.JwkService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType
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
    @Operation(
        summary = "JWKsエンドポイント", description = "RFC7517", responses = [ApiResponse(
            responseCode = "200",
            description = "Json Web Key Setを返します",
            content = [
                Content(
                    examples = [
                        ExampleObject(
                            value = """{
  "keys": [
    {
      "kty": "RSA",
      "e": "AQAB",
      "kid": "nEpO3EdePSSOWX-2zjpizXBWtmcY2yB4-XbVGJJK8Oo",
      "n": "9DfhZ92pIYxtxfWKpRuWAB1ybQbhfHhFgXtHsxVkmWMCyL5sa5RXsEXxiwmGQsCVN5pHAP2t0-GgeURgDOR1ugVdQwbQGX0qrFPSDtTe3BcdOFIGnbUtikDcVUj_PcgW3QWISNzZzSy_c6JayEIswhvNUReTiz3r5UFpKQ2N2CCCcflknFSU-_qX9MvSbge0zjjnqNd41KIBafdvlWD0LZS9LHbPA5rp1BfhX01NGPGs5NP8DczzEwYc-011SIULcMA7sU2B4DoxqGHDyprddhWM0PkeuSg3l1RUbtx1NEgS2WmJDt9snrrL6PQfftLo9hsHNhEMQbvLDUwYnX7Tow"
    }
  ]
}"""
                        )
                    ]
                )
            ]
        )]
    )
    @GetMapping("jwks.json", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun jwks(): ResponseEntity<Map<String?, Any?>?> {
        return ResponseEntity.ok(jwkService.jwks().toPublicJWKSet().toJSONObject())
    }
}