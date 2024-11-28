package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.entity.User

interface UserService {
    fun findByGoogleId(googleId: String): User?
}