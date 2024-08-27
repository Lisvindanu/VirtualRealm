package com.virtualrealm.our.gameMarketPlaces.model.inventory

data class UseInventoryItemRequest(
    val userId: Long,
    val itemId: Long,
    val quantity: Int
)
