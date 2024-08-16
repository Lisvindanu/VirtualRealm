package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "username")
    val username: String,

    @Column(name = "email")
    @field:NotBlank
    val email: String,
    @Column(name = "password")
    val password : String,
    @Column(name = "created_at")
    val createdAt : LocalDateTime = LocalDateTime.now(),

    )

