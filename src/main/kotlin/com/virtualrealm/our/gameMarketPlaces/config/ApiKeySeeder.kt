package com.virtualrealm.our.gameMarketPlaces.config

import com.virtualrealm.our.gameMarketPlaces.entity.ApiKey
import com.virtualrealm.our.gameMarketPlaces.repository.ApiKeyRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApiKeySeeder(val apiKeyRepository: ApiKeyRepository) : ApplicationRunner {
    val apiKey = "secret"
    override fun run(args: ApplicationArguments?) {
        if(!apiKeyRepository.existsById(apiKey)) {
            val entity = ApiKey(id = apiKey)
            apiKeyRepository.save(entity)
        }
    }
}