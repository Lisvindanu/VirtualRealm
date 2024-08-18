package com.virtualrealm.our.gameMarketPlaces.model.payment

import java.util.*

data class InitiatePaymentResponse(
    val paymentId: Long,
    val status: String,
    val amount: Long,
    val createdAt: Date
)
