package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.ApiKey
import org.springframework.data.jpa.repository.JpaRepository

interface ApiKeyRepository: JpaRepository<ApiKey, String> {
}