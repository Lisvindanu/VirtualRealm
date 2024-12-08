package com.virtualrealm.our.gameMarketPlaces

import io.github.cdimascio.dotenv.Dotenv
import jakarta.annotation.PostConstruct
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class TestConfig {
    @PostConstruct
    fun init() {
        val dotenv = Dotenv.configure().load()
        dotenv.entries().forEach { System.setProperty(it.key, it.value) }
    }
}
