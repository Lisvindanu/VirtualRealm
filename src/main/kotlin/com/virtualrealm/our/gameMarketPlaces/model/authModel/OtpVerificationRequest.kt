package com.virtualrealm.our.gameMarketPlaces.model.authModel

data class OtpVerificationRequest(
    val userId:Long,
    val otp:String
)
