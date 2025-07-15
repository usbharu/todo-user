package dev.usbharu.todouser.application.auth

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import dev.usbharu.todouser.application.jwk.JwkService.Companion.genKey
import dev.usbharu.todouser.config.JwtKeys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Jwtに署名するサービス
 */
@Service
class JwtSigner(@Autowired(required = false)  jwtKeys: JwtKeys = genKey()) {

    val signer = RSASSASigner(jwtKeys.privateKey)

    fun sign(jwtClaimsSet: JWTClaimsSet): SignedJWT {
        val header = JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build()
        logger.debug("Signing JWT with claims: {}", jwtClaimsSet.claims)
        val signedJWT = SignedJWT(header, jwtClaimsSet)
        signedJWT.sign(signer)
        return signedJWT
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtSigner::class.java)
    }
}