package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "purchase")
data class Purchase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false)
    val totalPrice: Long,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
