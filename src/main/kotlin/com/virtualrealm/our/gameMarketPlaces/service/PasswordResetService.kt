package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.entity.User

interface PasswordResetService {


    fun generateResetToken(userId: Long): String

    fun validateResetToken(token: String): User

    fun resetPassword(token: String, newPassword: String)
}