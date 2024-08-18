package com.virtualrealm.our.gameMarketPlaces.model.purchase

import java.time.LocalDateTime

data class PurchaseResponse(
    val purchaseId: Long,
    val userId: Long,
    val productId: Long,
    val quantity: Int,
    val totalPrice: Long,
    val createdAt: LocalDateTime
)
