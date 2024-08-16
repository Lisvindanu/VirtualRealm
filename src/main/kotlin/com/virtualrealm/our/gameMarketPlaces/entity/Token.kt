package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tokens")
data class Token(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "username")
    val username: String,
    @Column(name = "token")
    val token: String,
    @Column(name = "expires_at")
    val expiresAt: LocalDateTime
)
