package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.ApiKey
import org.springframework.data.jpa.repository.JpaRepository

@EnableJpaRepositories
interface ApiKeyRepository: JpaRepository<ApiKey, String> {
}