package com.virtualrealm.our.gameMarketPlaces.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    val id : Long? = null,
    @Column(name = "name")
    var name: String,
    @Column(name = "price")
    var price: Long,
    @Column(name = "quantity")
    var quantity: Int,
    @Column(name = "created_at")
    var createdAt: Date,
    @Column(name = "updated_at")
    var updatedAt: Date


    )
