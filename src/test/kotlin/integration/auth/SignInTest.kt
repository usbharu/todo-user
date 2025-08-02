package integration.auth

import dev.usbharu.todouser.TodoUserApplication
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.isA
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ContextConfiguration(classes = [TodoUserApplication::class])
@AutoConfigureMockMvc
@Rollback
@Transactional
class SignInTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun signInFromJson() {
        mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"username": "abcdefgh","password": "a"}"""
            }
            .andDo { print() }
            .andExpect { status { isOk() } }

        mockMvc
            .post("/auth/v1/sign_in") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"username":"abcdefgh","password":"a"}"""
            }
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpectAll {
                jsonPath("$.token", isA<String>(String::class.java))
            }
    }

    @Test
    fun signInFromJson_validation_error() {
        mockMvc
            .post("/auth/v1/sign_in") {
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
                MockMvcResultMatchers.jsonPath("$.errors").isArray
                jsonPath("$.errors", hasSize<Int>(1))
            }
    }

    @Test
    fun signInFromJson_InvalidPassword_error() {
        mockMvc
            .post("/auth/v1/sign_up") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"username": "abcdefgh","password": "a"}"""
            }
            .andDo { print() }
            .andExpect { status { isOk() } }

        mockMvc
            .post("/auth/v1/sign_in") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"username": "abcdefgh","password": "b"}"""
            }
            .andDo { print() }
            .andExpect { status { isUnauthorized() } }
            .andExpect { content { contentType(MediaType.APPLICATION_PROBLEM_JSON) } }
    }

    @Test
    fun signInFromJson_userNotFound_error() {
        mockMvc
            .post("/auth/v1/sign_in") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"username": "asjfakjffdsa","password": "b"}"""
            }
            .andDo { print() }
            .andExpect { status { isUnauthorized() } }
            .andExpect { content { contentType(MediaType.APPLICATION_PROBLEM_JSON) } }
    }
}