package com.virtualrealm.our.gameMarketPlaces.model.authModel

import java.time.LocalDateTime

data class RegisterResponseData(
    val id: String,
    val username: String,
    val email: String,
    val created_at: LocalDateTime
)

