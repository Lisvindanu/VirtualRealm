package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.entity.User
import com.virtualrealm.our.gameMarketPlaces.entity.UserToken
import com.virtualrealm.our.gameMarketPlaces.model.authModel.*

interface AuthServices {
    fun register(registerRequest: RegisterRequest): RegisterResponseData
    fun login(loginRequest: LoginRequest): LoginResponseData
    fun logout(token: String)
    fun getUserData(token: String): UserDataResponse
    fun exchangeAuthCodeForToken(code: String): String
    fun registerOrLoginWithGoogle(userData: UserDataResponse): RegisterResponseData
    fun generateAndStoreToken(user: User): UserToken
    fun getUserDataFromGoogleToken(googleToken: String): UserDataResponse
}
