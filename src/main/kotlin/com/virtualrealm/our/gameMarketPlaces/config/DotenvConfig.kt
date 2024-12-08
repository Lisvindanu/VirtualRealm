package com.virtualrealm.our.gameMarketPlaces.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DotenvConfig {

    @Bean
    fun dotenv(): Dotenv {
        val dotenv = Dotenv.configure().load()
        // Optional: Print loaded environment variables for debugging
        println("Loaded SMTP Host: ${dotenv["SMTP_HOST"]}")
        return dotenv
    }
}