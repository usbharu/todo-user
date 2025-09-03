package integration.user

import Utils
import com.fasterxml.jackson.databind.ObjectMapper
import dev.usbharu.todouser.TodoUserApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ContextConfiguration(classes = [TodoUserApplication::class])
@AutoConfigureMockMvc
@Rollback
@Transactional
class GetITest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun getI() {
        val userId = Utils.createUser(mockMvc, objectMapper).userId

        mockMvc
            .get("/api/v1/users/i") {
                with(jwt().jwt { builder -> builder.subject(userId.id.toString()) })
            }
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun getI_noToken_error() {
        mockMvc
            .get("/api/v1/users/i")
            .andDo { print() }
            .andExpect { status { isUnauthorized() } }
            .andExpect { content { contentType(MediaType.APPLICATION_PROBLEM_JSON) } }
    }
}