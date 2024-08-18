package com.virtualrealm.our.gameMarketPlaces.model.payment

import java.util.*

data class PaymentDetailResponse(val paymentId: Long,
                                 val amount: Long,
                                 val status: String,
                                 val createdAt: Date,
                                 val confirmedAt: Date?,
                                 val canceledAt: Date?,
                                 val reason: String?)
