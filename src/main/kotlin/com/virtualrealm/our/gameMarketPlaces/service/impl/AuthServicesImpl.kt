package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.virtualrealm.our.gameMarketPlaces.entity.User
import com.virtualrealm.our.gameMarketPlaces.entity.UserToken
import com.virtualrealm.our.gameMarketPlaces.model.authModel.*
import com.virtualrealm.our.gameMarketPlaces.repository.TokenRepository
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
import com.virtualrealm.our.gameMarketPlaces.service.AuthServices
import com.virtualrealm.our.gameMarketPlaces.validation.ValidationUtil
import org.mindrot.jbcrypt.BCrypt
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
        if(userRepository.findByEmail(registerRequest.email) != null) {
            throw IllegalArgumentException("User email already exists")
        }
        if (userRepository.findByUsername(registerRequest.username) != null) {
            throw IllegalArgumentException("User username already exists")
        }
        val hashedPassword = hashPassword(registerRequest.password)
        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = hashedPassword
        )
        val savedUser = userRepository.save(user)
        return convertUserToResponseData(savedUser)
    }

    override fun login(loginRequest: LoginRequest): LoginResponseData {
        validationUtil.validate(loginRequest)
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw IllegalArgumentException("Invalid Credentials")
        if(!checkPassword(loginRequest.password, user.password)) {
            throw IllegalArgumentException("Invalid Credentials")
        }
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
                tokenRepository.delete(userToken)
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

    override fun getUserData(token: String): UserDataResponse {
        logger.info("Fetching user data for token: $token")
        val userToken = tokenRepository.findByToken(token) ?: throw IllegalArgumentException("Invalid Token")
        val user = userToken.user ?: throw IllegalArgumentException("User not found")
        return UserDataResponse(username = user.username, email = user.email)
    }



    private fun convertUserToResponseData(user: User): RegisterResponseData {
        return RegisterResponseData(
            id = user.id.toString(),
            username = user.username,
            email = user.email,
            created_at = user.createdAt
        )
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
    fun checkPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }

}