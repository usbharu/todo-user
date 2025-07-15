package dev.usbharu.todouser2.config

import dev.usbharu.todouser2.application.jwk.JwkService.Companion.genKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity()
class SpringSecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/.well-known/**", permitAll)
                authorize("/api/**", authenticated)
                authorize(anyRequest, permitAll)
            }
            csrf {
                ignoringRequestMatchers({ request ->
                    val contentType = request.contentType
                    contentType.equals("application/json", true);
                })
            }
            oauth2ResourceServer {
                jwt {
                    jwt {  }
                }
            }
        }
        return http.build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder,
    ): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        return ProviderManager(authenticationProvider)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun jwtDecoder(@Autowired(required = false) jwtKeys: JwtKeys?  = genKey()): JwtDecoder {
        val keys = if (jwtKeys == null) {
            genKey()
        } else {
            jwtKeys
        }
        return NimbusJwtDecoder.withPublicKey(keys.publicKey).build()
    }
}