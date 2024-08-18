package com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel

import java.util.*

data class ProductResponse(
    val id: Long?,
    val name: String,
    val price: Long,
    val quantity: Int,
    val created_at: Date,
    val updated_at: Date?

    )
