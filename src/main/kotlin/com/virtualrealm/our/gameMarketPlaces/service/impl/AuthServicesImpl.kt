package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.virtualrealm.our.gameMarketPlaces.entity.User
import com.virtualrealm.our.gameMarketPlaces.entity.UserToken
import com.virtualrealm.our.gameMarketPlaces.model.LoginRequest
import com.virtualrealm.our.gameMarketPlaces.model.LoginResponseData
import com.virtualrealm.our.gameMarketPlaces.model.RegisterRequest
import com.virtualrealm.our.gameMarketPlaces.model.RegisterResponseData
import com.virtualrealm.our.gameMarketPlaces.repository.TokenRepository
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
import com.virtualrealm.our.gameMarketPlaces.service.AuthServices
import com.virtualrealm.our.gameMarketPlaces.validation.ValidationUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
@Service
class AuthServicesImpl(
    val userRepository: UserRepository,
    val validationUtil: ValidationUtil,
    private val tokenRepository: TokenRepository
) : AuthServices {

    override fun register(registerRequest: RegisterRequest): RegisterResponseData {
        validationUtil.validate(registerRequest)
        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = registerRequest.password
        )
        val savedUser = userRepository.save(user)
        return convertUserToResponseData(savedUser)
    }

    override fun login(loginRequest: LoginRequest): LoginResponseData {
        validationUtil.validate(loginRequest)
        val user = userRepository.findByUsernameAndPassword(loginRequest.username, loginRequest.password)
            ?: throw IllegalArgumentException("Invalid Credentials")

        val token = UUID.randomUUID().toString()
        val userToken = UserToken(
            username = user.username,
            token = token,
            expiresAt = LocalDateTime.now().plusHours(1),
            user = user  // Menyimpan referensi ke user
        )
        tokenRepository.save(userToken)

        return LoginResponseData(token = token, expiresAt = userToken.expiresAt)
    }

    private val logger = LoggerFactory.getLogger(AuthServicesImpl::class.java)

    override fun logout(token: String) {
        try {
            logger.info("Attempting to logout with token: $token")
            val userToken = tokenRepository.findByToken(token)
            if (userToken != null) {
                tokenRepository.delete(userToken) // Menggunakan metode delete bawaan JpaRepository
                logger.info("Token deleted successfully.")
            } else {
                logger.error("Token not found: $token")
                throw IllegalArgumentException("Token not found")
            }
        } catch (ex: Exception) {
            logger.error("Error occurred during logout: ${ex.message}", ex)
            throw ex
        }
    }



    private fun convertUserToResponseData(user: User): RegisterResponseData {
        return RegisterResponseData(
            id = user.id.toString(),
            username = user.username,
            email = user.email,
            created_at = user.createdAt
        )
    }
}