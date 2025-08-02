import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.usbharu.todouser.application.users.UserDetail
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

object Utils {
    fun createUser(mockMvc: MockMvc, objectMapper: ObjectMapper = jacksonObjectMapper()): UserDetail {
        val contentAsString = mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {"username":"abcdefgh","password":"a"}
                """.trimIndent()
            }
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andReturn().response.contentAsString

        val userDetail = objectMapper.readValue(contentAsString, UserDetail::class.java)
        return userDetail
    }
}