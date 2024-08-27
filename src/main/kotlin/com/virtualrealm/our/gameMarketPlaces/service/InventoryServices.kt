package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.model.inventory.GetUserInventoryItemResponse
import com.virtualrealm.our.gameMarketPlaces.model.inventory.UseInventoryItemRequest

interface InventoryServices {
    fun getUserInventory(userId:Long) : GetUserInventoryItemResponse
    fun useInventoryItem(request: UseInventoryItemRequest): GetUserInventoryItemResponse
    fun updateInventoryAfterPurchase(userId: Long, itemId: Long, quantity: Int): Boolean
}