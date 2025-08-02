package dev.usbharu.todouser

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
class TodoUserApplicationTests {

    @Test
    fun contextLoads() {
    }
}

@Suppress("ClassName")
@TestPropertySource(properties = ["application.auth.jwt.public-key=classpath:public_key.pem", "application.auth.jwt.private-key=classpath:private_key.pem"])
@SpringBootTest
class TodoUserApplicationTests__LoadKeyFromClasspath {
    @Test
    fun contextLoads() {
    }
}
