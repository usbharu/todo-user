package dev.usbharu.todouser.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.collections.set

@Configuration
class OpenApiConfig(@field:Autowired(required = false) private val buildProperties: BuildProperties? = null) {
    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            val validationErrorDetailSchema = ObjectSchema()
                .type("object")
                .addProperty("field", StringSchema())
                .addProperty("message", StringSchema().nullable(true))
                .addProperty("rejectedValue", StringSchema().nullable(true))
                .required(listOf("field"))

            val problemDetailSchema = ObjectSchema()
                .type("object")
                .addProperty("type", StringSchema())
                .addProperty("title", StringSchema())
                .addProperty("status", Schema<Int>().type("integer"))
                .addProperty("detail", StringSchema())
                .addProperty("instance", StringSchema())
                .addProperty("errors", ArraySchema().items(validationErrorDetailSchema))
                .required(listOf("type", "title", "status", "detail", "instance"))

            openApi.components.schemas["ValidationErrorDetail"] = validationErrorDetailSchema
            openApi.components.schemas["ProblemDetail"] = problemDetailSchema
        }
    }

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
