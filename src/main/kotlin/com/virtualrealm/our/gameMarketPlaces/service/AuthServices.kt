package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.model.authModel.*

interface AuthServices {
    fun register(registerRequest: RegisterRequest) : RegisterResponseData
    fun login(loginRequest: LoginRequest) : LoginResponseData
    fun logout(token: String)
    fun getUserData(token: String): UserDataResponse

}