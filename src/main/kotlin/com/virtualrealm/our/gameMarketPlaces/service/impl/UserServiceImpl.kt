package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.virtualrealm.our.gameMarketPlaces.entity.User
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
import com.virtualrealm.our.gameMarketPlaces.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun findByGoogleId(googleId: String): User? {
        return userRepository.findByGoogleId(googleId)
    }
}
