package dev.usbharu.todouser.config

import dev.usbharu.todouser.application.jwk.JwkService.Companion.genKey
import dev.usbharu.todouser.infra.MdcXRequestIdFilter
import dev.usbharu.todouser.infra.ProblemDetailsAccessDeniedHandler
import dev.usbharu.todouser.infra.ProblemDetailsAuthenticationEntryPoint
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.context.SecurityContextHolderFilter
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher

@Configuration
@EnableWebSecurity(debug = false)
@SecurityScheme(
    name = "jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    description = "JWTを利用したトークン",
    bearerFormat = "JWT"
)
class SpringSecurityConfig {

    @Order(1)
    @Bean
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity, problemDetailsAuthenticationEntryPoint: ProblemDetailsAuthenticationEntryPoint,
        problemDetailsAccessDeniedHandler: ProblemDetailsAccessDeniedHandler,
    ): SecurityFilterChain {
        val authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer();
        http {
            securityMatcher(authorizationServerConfigurer.endpointsMatcher)
            with(authorizationServerConfigurer) {
                authorizationServerConfigurer.oidc(org.springframework.security.config.Customizer.withDefaults())
            }
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
            exceptionHandling {
                defaultAuthenticationEntryPointFor(
                    LoginUrlAuthenticationEntryPoint("/sign_in?source=oauth2"),
                    MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            }
            oauth2ResourceServer {
                jwt {

                }
                authenticationEntryPoint = problemDetailsAuthenticationEntryPoint
                accessDeniedHandler = problemDetailsAccessDeniedHandler
            }
        }
        return http.build()
    }

    @Bean
    @Order(2)
    fun securityFilterChain(
        http: HttpSecurity,
        problemDetailsAuthenticationEntryPoint: ProblemDetailsAuthenticationEntryPoint,
        problemDetailsAccessDeniedHandler: ProblemDetailsAccessDeniedHandler,
    ): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/.well-known/**", permitAll)
                authorize("/api/**", authenticated)
                authorize(anyRequest, permitAll)
            }
            csrf {
                ignoringRequestMatchers({ request ->
                    val contentType = request.contentType
                    contentType.equals("application/json", true)
                })
            }
            oauth2ResourceServer {
                jwt {
                }
                authenticationEntryPoint = problemDetailsAuthenticationEntryPoint
                accessDeniedHandler = problemDetailsAccessDeniedHandler
            }
            addFilterBefore<SecurityContextHolderFilter>(MdcXRequestIdFilter(requestIdKey, requestIdHeaderName))
            exceptionHandling {
                authenticationEntryPoint = problemDetailsAuthenticationEntryPoint
                accessDeniedHandler = problemDetailsAccessDeniedHandler
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
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
    fun jwtDecoder(@Autowired(required = false) jwtKeys: JwtKeys? = genKey()): JwtDecoder {
        val keys = if (jwtKeys == null) {
            genKey()
        } else {
            jwtKeys
        }
        return NimbusJwtDecoder.withPublicKey(keys.publicKey).build()
    }

    @Value("\${application.logging.request-id-header:X-REQUEST-ID}")
    private lateinit var requestIdHeaderName: String

    @Value("\${application.logging.request-id-key:request_id}")
    private lateinit var requestIdKey: String

    @Bean
    fun authorizationServerSetting(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().build()
    }
}
