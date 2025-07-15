package dev.usbharu.todouser.application.auth

import com.nimbusds.jwt.JWTClaimsSet
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

/**
 * ログインのアプリケーションサービス
 */
@Service
class SignInService(private val authenticationManager: AuthenticationManager, private val jwtSigner: JwtSigner) {
    suspend fun signIn(username: String, password: String): String {
        val authenticate =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password))

        val build = JWTClaimsSet.Builder()
            .issuer("user")
            .claim("scope", setOf("read", "write"))
            .subject(username)
            .build()

        val signedJWT = jwtSigner.sign(build)

        return signedJWT.serialize()
    }
}