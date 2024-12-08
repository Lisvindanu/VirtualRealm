package com.virtualrealm.our.gameMarketPlaces.model.authModel

enum class OtpStatus(val status: String, val message: String) {
    OTP_REQUIRED("otp_required", "OTP required for verification"),
    OTP_SENT("otp_sent", "OTP has been sent to your email"),
    OTP_ALREADY_SENT("success", "OTP has already been sent"),
    OTP_VERIFIED("otp_verified", "OTP verified successfully"),
    OTP_FAILED("otp_failed", "Invalid or expired OTP");
}
