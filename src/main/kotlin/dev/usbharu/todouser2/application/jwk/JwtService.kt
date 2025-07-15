package dev.usbharu.todouser2.application.jwk

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import dev.usbharu.todouser2.config.JwtKeys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Service
class JwtService(@Autowired(required = false) val jwtKeys: JwtKeys = genKey()) {
    fun jwks(): JWKSet {
        val rSAKey = RSAKey.Builder(jwtKeys.publicKey)
            .keyIDFromThumbprint()
            .build()
        return JWKSet(rSAKey.toPublicJWK())
    }

    companion object {
        private var jwtKeys : JwtKeys? = null
        fun genKey(): JwtKeys {
            jwtKeys?.let { return it }
            val keyGenerator = KeyPairGenerator.getInstance("RSA")
            val keyPair = keyGenerator.genKeyPair()
            val rsaPublicKey = keyPair.public as RSAPublicKey
            val rsaPrivateKey = keyPair.private as RSAPrivateKey
            val jwtKeys = JwtKeys(rsaPublicKey, rsaPrivateKey)
            this.jwtKeys = jwtKeys
            return jwtKeys
        }
    }
}