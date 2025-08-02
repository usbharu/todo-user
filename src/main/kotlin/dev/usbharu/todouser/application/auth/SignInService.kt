package dev.usbharu.todouser.application.auth

import com.nimbusds.jwt.JWTClaimsSet
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

/**
 * ログインのアプリケーションサービス
 */
@Service
class SignInService(private val authenticationManager: AuthenticationManager, private val jwtSigner: JwtSigner) {
    fun signIn(username: String, password: String): String {
        logger.debug("Signing in with username: {}", username)
        val authenticate =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password))
        logger.debug("Successfully password authenticate")
        val userDetails = authenticate.principal as? CustomUserDetails
        if (userDetails == null) {
            throw IllegalStateException("userDetails is null")
        }
        val build = JWTClaimsSet.Builder()
            .issuer("user")
            .claim("scope", setOf("read", "write"))
            .subject(userDetails.userId.id.toString())
            .build()

        val signedJWT = jwtSigner.sign(build)
        logger.debug("Successfully signed JWT")
        return signedJWT.serialize()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SignInService::class.java)
    }
}
