package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Column(name = "username", unique = true)
    var username: String,

    @Column(name = "email")
    @field:NotBlank
    val email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "googleId")
    val googleId: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "isOtpVerified")
    var isOtpVerified: Boolean? = false,

    @Column(name = "role")
    var role: String ?= "USER"
)
