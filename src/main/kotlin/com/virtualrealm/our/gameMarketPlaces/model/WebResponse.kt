package com.virtualrealm.our.gameMarketPlaces.model

data class WebResponse<T>(
    val code: Int,
    val status: String,
    val data: T
)

typealias LoginResponse = WebResponse<LoginResponseData>
typealias RegisterResponse = WebResponse<RegisterResponseData>
typealias LogoutResponse = WebResponse<Void>

