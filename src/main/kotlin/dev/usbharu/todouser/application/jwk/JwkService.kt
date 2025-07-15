package dev.usbharu.todouser.application.jwk

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import dev.usbharu.todouser.config.JwtKeys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

/**
 * Jwksの生成サービス
 */
@Service
class JwkService(@Autowired(required = false) val jwtKeys: JwtKeys = genKey()) {
    fun jwks(): JWKSet {
        val rSAKey = RSAKey.Builder(jwtKeys.publicKey)
            .keyIDFromThumbprint()
            .build()
        return JWKSet(rSAKey.toPublicJWK())
    }

    companion object {
        private var jwtKeys : JwtKeys? = null
        fun genKey(): JwtKeys {
            jwtKeys?.let {
                logger.trace("Use cached JWKs")
                return it
            }
            logger.info("Generating JWKs...")
            val keyGenerator = KeyPairGenerator.getInstance("RSA")
            val keyPair = keyGenerator.genKeyPair()
            val rsaPublicKey = keyPair.public as RSAPublicKey
            val rsaPrivateKey = keyPair.private as RSAPrivateKey
            val jwtKeys = JwtKeys(rsaPublicKey, rsaPrivateKey)
            this.jwtKeys = jwtKeys
            logger.info("Generated JWKs successfully.")
            return jwtKeys
        }

        private val logger = LoggerFactory.getLogger(JwkService::class.java)
    }
}