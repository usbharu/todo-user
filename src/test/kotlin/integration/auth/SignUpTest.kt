package integration.auth

import dev.usbharu.todouser.TodoUserApplication
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ContextConfiguration(classes = [TodoUserApplication::class])
@AutoConfigureMockMvc
@Rollback
@Transactional
class SignUpTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun signUpFromJson() {
        mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """{
  "username": "abcdefgh",
  "password": "a"
}"""
            }
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }
            .andExpectAll {
                jsonPath("$.username", isA<String>(String::class.java))
                jsonPath("$.username", `is`("abcdefgh"))
                jsonPath("$.userId", isA<String>(String::class.java))
                jsonPath("$.createdAt", isA<String>(String::class.java))
            }
    }

    @Test
    fun signUpFromJson_validation_error() {
        mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {
                      "username": "あ",
                      "password": "a"
                    }
                """.trimIndent()
            }
            .andDo { print() }
            .andExpect { status { isBadRequest() } }
            .andExpect { content { contentType(MediaType.APPLICATION_PROBLEM_JSON) } }
            .andExpectAll {
                jsonPath("$.errors").isArray
                jsonPath("$.errors", hasSize<Int>(1))
            }
    }

    @Test
    fun signUpFromJson_InvalidRequestBody_error() {
        mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"username": "abcdefgh"}"""
            }
            .andDo { print() }
            .andExpect { content { contentType(MediaType.APPLICATION_PROBLEM_JSON) } }
            .andExpect { status { isBadRequest() } }
    }

    @Test
    fun signUpFromJson_usernameAlreadyUsed_error() {
        mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """{
  "username": "abcdefgh",
  "password": "a"
}"""
            }
            .andDo { print() }
            .andExpect { status { isOk() } }
        mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"username": "abcdefgh","password": "a"}"""
            }
            .andDo { print() }
            .andExpect { content { contentType(MediaType.APPLICATION_PROBLEM_JSON) } }
            .andExpect { status { isBadRequest() } }
    }
}