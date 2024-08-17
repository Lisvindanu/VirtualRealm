package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tokens")
data class UserToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "username")
    val username: String,

    @Column(unique = true)
    val token: String,

    @Column(name = "expires_at")
    val expiresAt: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    val user: User? = null
)
