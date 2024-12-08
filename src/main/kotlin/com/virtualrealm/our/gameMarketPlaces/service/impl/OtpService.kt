package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.virtualrealm.our.gameMarketPlaces.entity.OtpToken
import com.virtualrealm.our.gameMarketPlaces.repository.OtpTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class OtpService(private val otpTokenRepository: OtpTokenRepository) {

    fun generateOtp(userId: Long): String {
        val otp = (100000..999999).random().toString() // Menghasilkan OTP 6 digit
        val expiresAt = LocalDateTime.now().plusMinutes(5)

        // Hapus OTP lama sebelum menyimpan yang baru
        otpTokenRepository.deleteByUserId(userId)

        // Simpan OTP baru
        otpTokenRepository.save(OtpToken(
            userId = userId,
            otp = otp,
            expiresAt = expiresAt,
        ))

        return otp
    }

    fun generateOtpKetikaDibutuhkan(userId: Long): String {
        val existingOtp = otpTokenRepository.findByUserIdAndExpiresAtGreaterThan(userId, LocalDateTime.now())

        if (existingOtp != null && existingOtp.expiresAt.isAfter(LocalDateTime.now())) {
            return existingOtp.otp
        }

        return generateOtp(userId)
    }

    fun validateOtp(userId: Long, otp: String): Boolean {
        val otpToken = otpTokenRepository.findByUserIdAndOtp(userId, otp).orElse(null)
        ?: return false

        return !otpToken.expiresAt.isBefore(LocalDateTime.now())
    }

    fun getActiveOtpForUser (userId: Long): OtpToken? {
        // Ambil OTP aktif untuk pengguna
        return otpTokenRepository.findByUserIdAndExpiresAtGreaterThan(userId, LocalDateTime.now())
    }

    fun deleteOtp(otpToken: OtpToken) {
        otpTokenRepository.delete(otpToken)
    }
}