package com.virtualrealm.our.gameMarketPlaces.model.payment

import jakarta.validation.constraints.NotBlank

data class ConfirmPaymentRequest(
    @field:NotBlank
    var status: String
)
