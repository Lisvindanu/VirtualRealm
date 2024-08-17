package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.UserToken
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository: JpaRepository<UserToken,Long> {
    fun findByToken(token: String): UserToken?
    fun deleteByToken(token: String)
}