package com.virtualrealm.our.gameMarketPlaces.model.inventory

data class UseInventoryItemResponseData(
    val userId: Long,
    val itemId: Long,
    val quantityUsed: Int,
    val remainingQuantity: Int,
    val updatedAt: String
)
