package dev.usbharu.todouser2.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

/**
 * PublicKeyを外部から読み込むときに使用するプロパティ.
 *
 * application.auth.jwt.publicKey=classpath:/path/to/publickey.pem
 */
@ConfigurationProperties("application.auth.jwt", ignoreUnknownFields = true, ignoreInvalidFields = true)
data class JwtKeys(
    val publicKey: RSAPublicKey,
    val privateKey: RSAPrivateKey,
)
