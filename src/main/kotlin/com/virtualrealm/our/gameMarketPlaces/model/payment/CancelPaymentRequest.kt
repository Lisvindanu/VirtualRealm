package com.virtualrealm.our.gameMarketPlaces.model.payment

import jakarta.validation.constraints.NotBlank

data class CancelPaymentRequest(
    @field:NotBlank
    val reason: String
)
