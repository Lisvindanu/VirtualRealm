package com.virtualrealm.our.gameMarketPlaces.model

import com.virtualrealm.our.gameMarketPlaces.model.authModel.LoginResponseData
import com.virtualrealm.our.gameMarketPlaces.model.authModel.RegisterResponseData
import com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel.ProductResponse
import com.virtualrealm.our.gameMarketPlaces.model.payment.InitiatePaymentResponse

data class WebResponse<T>(
    val code: Int,
    val status: String,
    val data: T,
)

typealias LoginResponse = WebResponse<LoginResponseData>
typealias RegisterResponse = WebResponse<RegisterResponseData>
typealias LogoutResponse = WebResponse<Void>
typealias ProductResponse = WebResponse<ProductResponse>
typealias InitiatePaymentResponse = WebResponse<InitiatePaymentResponse>

