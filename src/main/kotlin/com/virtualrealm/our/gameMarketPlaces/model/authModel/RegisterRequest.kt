package com.virtualrealm.our.gameMarketPlaces.model.authModel

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String? = null,
    val isGoogle: Boolean = false,  // Flag for Google registration
    val googleToken: String? = null // Google OAuth token
)
