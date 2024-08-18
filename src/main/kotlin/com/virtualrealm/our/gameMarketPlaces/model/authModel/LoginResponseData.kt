package com.virtualrealm.our.gameMarketPlaces.model.authModel

import java.time.LocalDateTime

data class LoginResponseData(
    val token:String,
    val expiresAt: LocalDateTime
)
