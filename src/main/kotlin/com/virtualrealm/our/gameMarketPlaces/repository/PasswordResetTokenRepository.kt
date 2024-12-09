package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {
    fun findByToken(token: String): Optional<PasswordResetToken>
    fun deleteByUserId(userId: Long)
}
