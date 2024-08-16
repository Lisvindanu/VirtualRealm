package com.virtualrealm.our.gameMarketPlaces.model

import java.time.LocalDateTime

data class LoginResponseData(
    val token:String,
    val expiresAt: LocalDateTime
)
