package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.Token
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository: JpaRepository<Token,Long> {
    fun findByToken(token: String): Token?
    fun deleteByToken(token: String)
}