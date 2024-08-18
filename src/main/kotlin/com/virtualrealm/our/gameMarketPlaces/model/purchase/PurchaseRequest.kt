package com.virtualrealm.our.gameMarketPlaces.model.purchase

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class PurchaseRequest(
    @field:NotNull
    @field:Min(1)
    val userId: Long,

    @field:NotNull
    @field:Min(1)
    val productId: Long,

    @field:NotNull
    @field:Min(1)
    val quantity: Int
)
