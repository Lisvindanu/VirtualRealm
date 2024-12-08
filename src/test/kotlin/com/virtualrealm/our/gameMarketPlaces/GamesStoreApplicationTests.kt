package com.virtualrealm.our.gameMarketPlaces

import io.github.cdimascio.dotenv.Dotenv
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource

@SpringBootTest(classes = [GamesStoreApplication::class, TestConfig::class])
class GamesStoreApplicationTests {

	companion object {
		@JvmStatic
		@DynamicPropertySource
		fun dynamicProperties(registry: DynamicPropertyRegistry) {
			val dotenv = Dotenv.configure().load()
			dotenv.entries().forEach { entry ->
				registry.add(entry.key) { entry.value }
			}
		}
	}


	@Test
	fun contextLoads() {
	}

}
