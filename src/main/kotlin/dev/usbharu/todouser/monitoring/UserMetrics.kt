package dev.usbharu.todouser.monitoring

import dev.usbharu.todouser.domain.users.UserRepository
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class UserMetrics(registry: MeterRegistry, private val userRepository: UserRepository) {
    init {
        Gauge
            .builder("users.count", this) { getUserCount() }
            .description("Total number of users")
            .register(registry)
    }

    private fun getUserCount(): Double {
        return userRepository.count().toDouble()
    }
}
