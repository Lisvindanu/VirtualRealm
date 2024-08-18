package com.virtualrealm.our.gameMarketPlaces.model.payment

import java.util.Date

data class ConfirmPaymentResponse (
    val paymentId: Long,
    val status: String,
    val confirmedAt: Date
)