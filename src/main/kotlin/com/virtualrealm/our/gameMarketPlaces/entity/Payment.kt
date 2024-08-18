package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.Date

@Entity
@Table(name = "payments")
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    val purchase: Purchase,
    @Column(nullable = false)
    val amount: Long,
    @Column(nullable = false)
    var status: String,
    @Column(nullable = false)
    val createdAt: Date,
    @Column(nullable = true)
    var confirmedAt: Date? = null,
    @Column(nullable = true)
    var canceledAt: Date? = null,
    @Column(nullable = true)
    var reason: String? = null
)
