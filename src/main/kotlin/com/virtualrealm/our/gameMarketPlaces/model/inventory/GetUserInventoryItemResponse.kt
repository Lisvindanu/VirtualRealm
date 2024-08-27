package com.virtualrealm.our.gameMarketPlaces.model.inventory

data class GetUserInventoryItemResponse(
    val code: Int,
    val status: String,
    val data: List<InventoryItemResponse>
)
