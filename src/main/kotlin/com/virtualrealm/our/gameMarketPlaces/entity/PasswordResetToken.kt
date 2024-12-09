package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "password_reset_tokens")
data class PasswordResetToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "token", unique = true, nullable = false)
    val token: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "expires_at", nullable = false)
    val expiresAt: LocalDateTime
)
