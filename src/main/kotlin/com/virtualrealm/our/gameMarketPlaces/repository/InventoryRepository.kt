package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.Inventory
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryRepository: JpaRepository<Inventory, Long> {
    fun findByUserId(userId: Long): List<Inventory>
    fun findByUserIdAndProductId(userId: Long, productId: Long): Inventory?
}