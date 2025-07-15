package dev.usbharu.todouser2.application.auth

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import dev.usbharu.todouser2.application.jwk.JwtService.Companion.genKey
import dev.usbharu.todouser2.config.JwtKeys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class JwtSigner(@Autowired(required = false)  jwtKeys: JwtKeys = genKey()) {

    val signer = RSASSASigner(jwtKeys.privateKey)

    fun sign(jwtClaimsSet: JWTClaimsSet): SignedJWT {
        val header = JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build()

        val signedJWT = SignedJWT(header, jwtClaimsSet)
        signedJWT.sign(signer)
        return signedJWT
    }
}