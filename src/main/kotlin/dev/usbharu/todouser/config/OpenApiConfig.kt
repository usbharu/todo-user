package dev.usbharu.todouser.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig(@field:Autowired(required = false) private val buildProperties: BuildProperties? = null) {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("ToDoUser API")
                    .version(buildProperties?.version ?: "UNKNOWN") // ここでバージョンを設定
                    .description("TodoアプリバックエンドのUserと認証に関するAPI")
            )
    }

}