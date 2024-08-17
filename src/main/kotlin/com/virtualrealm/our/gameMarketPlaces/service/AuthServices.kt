package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.model.LoginRequest
import com.virtualrealm.our.gameMarketPlaces.model.LoginResponseData
import com.virtualrealm.our.gameMarketPlaces.model.RegisterRequest
import com.virtualrealm.our.gameMarketPlaces.model.RegisterResponseData

interface AuthServices {
    fun register(registerRequest: RegisterRequest) : RegisterResponseData
    fun login(loginRequest: LoginRequest) : LoginResponseData
    fun logout(token: String)
}