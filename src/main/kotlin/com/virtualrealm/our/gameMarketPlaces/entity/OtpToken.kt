package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "otp_token", uniqueConstraints = [
    UniqueConstraint(columnNames = ["user_id", "otp"])
])
data class OtpToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "otp",nullable = false)
    val otp: String,

    @Column(name = "expires_at",nullable = false)
    val expiresAt: LocalDateTime
)