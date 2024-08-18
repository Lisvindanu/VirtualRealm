package com.virtualrealm.our.gameMarketPlaces.model.payment

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class InitiatePaymentRequest(
    @field:NotNull
    @field:Min(1)
    val purchaseId: Long?,
    @field:NotNull
    @field:Min(1)
    val amount: Long
)
