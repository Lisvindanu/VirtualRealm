package com.virtualrealm.our.gameMarketPlaces

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.github.cdimascio.dotenv.Dotenv

@SpringBootApplication
class GamesStoreApplication

fun main(args: Array<String>) {
	val dotenv = Dotenv.configure().load()
	val requiredKeys = listOf("GOOGLE_CLIENT_ID", "GOOGLE_CLIENT_SECRET", "GOOGLE_REDIRECT_URI")

	requiredKeys.forEach { key ->
		requireNotNull(dotenv[key]) { "Environment variable $key is required but not set in .env" }
		System.setProperty(key, dotenv[key] ?: "")
	}
	runApplication<GamesStoreApplication>(*args)
}
