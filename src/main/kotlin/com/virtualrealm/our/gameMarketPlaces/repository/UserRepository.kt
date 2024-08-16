package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsernameAndPassword(username: String, password: String): User?
}