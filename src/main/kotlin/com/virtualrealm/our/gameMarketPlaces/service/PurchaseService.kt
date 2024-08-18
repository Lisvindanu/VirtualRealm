package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.entity.Purchase
import com.virtualrealm.our.gameMarketPlaces.model.purchase.PurchaseRequest

interface PurchaseService {
    fun createPurchase(request : PurchaseRequest): Purchase
    fun getPurchaseHistory(userId : Long) : List<Purchase>
}