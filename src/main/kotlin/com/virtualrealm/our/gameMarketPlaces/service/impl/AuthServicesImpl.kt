package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.virtualrealm.our.gameMarketPlaces.entity.User
import com.virtualrealm.our.gameMarketPlaces.entity.UserToken
import com.virtualrealm.our.gameMarketPlaces.model.authModel.*
import com.virtualrealm.our.gameMarketPlaces.repository.TokenRepository
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
import com.virtualrealm.our.gameMarketPlaces.service.AuthServices
import com.virtualrealm.our.gameMarketPlaces.validation.ValidationUtil
import io.github.cdimascio.dotenv.Dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import okhttp3.FormBody
import okhttp3.Response
import java.util.*


@Service
class AuthServicesImpl(
    val userRepository: UserRepository,
    val validationUtil: ValidationUtil,
    private val tokenRepository: TokenRepository
) : AuthServices {

    private val logger = LoggerFactory.getLogger(AuthServicesImpl::class.java)
    private val client = OkHttpClient()
    private val dotenv = Dotenv.load() // Load .env file

    private val googleClientId = dotenv["GOOGLE_CLIENT_ID"] ?: throw IllegalStateException("GOOGLE_CLIENT_ID not found in .env")
    private val googleClientSecret = dotenv["GOOGLE_CLIENT_SECRET"] ?: throw IllegalStateException("GOOGLE_CLIENT_SECRET not found in .env")
    private val redirectUri = dotenv["REDIRECT_URI"] ?: throw IllegalStateException("REDIRECT_URI not found in .env")

    override fun register(registerRequest: RegisterRequest): RegisterResponseData {
        validationUtil.validate(registerRequest)

        if (userRepository.findByEmail(registerRequest.email) != null) {
            throw IllegalArgumentException("User email already exists")
        }
        if (userRepository.findByUsername(registerRequest.username) != null) {
            throw IllegalArgumentException("User username already exists")
        }

        val password = if (registerRequest.isGoogle) {
            null
        } else {
            registerRequest.password ?: throw IllegalArgumentException("Password cannot be null")
        }
        val hashedPassword = password?.let { hashPassword(it) }
        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = hashedPassword ?: ""
        )
        val savedUser = userRepository.save(user)
        return convertUserToResponseData(savedUser)
    }

    override fun login(loginRequest: LoginRequest): LoginResponseData {
        validationUtil.validate(loginRequest)

        return if (loginRequest.isGoogle) {
            val googleToken = loginRequest.googleToken ?: throw IllegalArgumentException("Google token cannot be null")
            val userData = getUserDataFromGoogleToken(googleToken)
            logger.info("Google login successful for user: ${userData.username}")
            val user = userRepository.findByUsername(userData.username)
                ?: userRepository.save(User(username = userData.username, email = userData.email, password = ""))
            val token = generateAndStoreToken(user)

            logger.debug("Token generated: ${token.token}")

            LoginResponseData(token = token.token, expiresAt = token.expiresAt)
        } else {
            val user = userRepository.findByUsername(loginRequest.username)
                ?: throw IllegalArgumentException("Invalid Credentials")
            val password = loginRequest.password ?: throw IllegalArgumentException("Password cannot be null")
            if (!checkPassword(password, user.password)) {
                logger.warn("Invalid password for user: ${loginRequest.username}")
                throw IllegalArgumentException("Invalid Credentials")
            }
            val token = generateAndStoreToken(user)
            LoginResponseData(token = token.token, expiresAt = token.expiresAt)
        }
    }

    override fun registerOrLoginWithGoogle(userData: UserDataResponse): RegisterResponseData {
        val existingUser = userRepository.findByUsername(userData.username)
        val user = if (existingUser != null) {
            existingUser
        } else {
            val newUser = User(
                username = userData.username,
                email = userData.email,
                password = ""
            )
            userRepository.save(newUser)
            newUser
        }
        return convertUserToResponseData(user)
    }

    override fun getUserData(token: String): UserDataResponse {
        val userToken = tokenRepository.findByToken(token) ?: throw IllegalArgumentException("Invalid Token")
        val user = userToken.user ?: throw IllegalArgumentException("User not found")
        return UserDataResponse(username = user.username, email = user.email)
    }

    override fun logout(token: String) {
        val userToken = tokenRepository.findByToken(token)
        if (userToken != null) {
            tokenRepository.delete(userToken) // Delete the token if it exists
        } else {
            throw IllegalArgumentException("Token not found")
        }
    }

    // Helper method to get user data from Google token
    override fun getUserDataFromGoogleToken(googleToken: String): UserDataResponse {
        val userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo"
        val request = Request.Builder()
            .url(userInfoEndpoint)
            .addHeader("Authorization", "Bearer $googleToken")
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: throw IllegalArgumentException("Empty response body")
            val objectMapper = ObjectMapper()
            val jsonResponse = objectMapper.readTree(responseBody)

            val username = jsonResponse["name"].asText()
            val email = jsonResponse["email"].asText()

            logger.debug("Google token received: $googleToken")
            logger.debug("User data from Google: Username = $username, Email = $email")


            return UserDataResponse(username = username, email = email)
        } else {
            throw IllegalArgumentException("Failed to fetch user data from Google: ${response.message}")
        }
    }

    override fun exchangeAuthCodeForToken(code: String): String {
        val client = OkHttpClient()
        val tokenEndpoint = "https://oauth2.googleapis.com/token"

        val formBody = FormBody.Builder()
            .add("code", code)
            .add("client_id", googleClientId)
            .add("client_secret", googleClientSecret)
            .add("redirect_uri", redirectUri)
            .add("grant_type", "authorization_code")
            .build()

        val request = Request.Builder()
            .url(tokenEndpoint)
            .post(formBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    logger.error("Failed to exchange auth code for token: ${response.message}")
                    throw IllegalArgumentException("Invalid response: ${response.code}")
                }
                val responseBody = response.body?.string() ?: throw IllegalArgumentException("Empty response body")
                val tokenResponse = ObjectMapper().readTree(responseBody)
                return tokenResponse["access_token"].asText()
            }
        } catch (e: Exception) {
            logger.error("Error exchanging auth code: ${e.message}")
            throw RuntimeException("Error during token exchange", e)
        }
    }

//    override fun exchangeAuthCodeForToken(code: String): String {
//        val client = OkHttpClient()
//
//        // Google's token endpoint
//        val tokenEndpoint = "https://oauth2.googleapis.com/token"
//
//        // Your Google Client ID and Client Secret

//        val redirectUri = "http://localhost:8080/login/oauth2/code/google"  // This should match the redirect URI you registered in Google Developer Console
//
//        // Create the request body
//        val formBody = FormBody.Builder()
//            .add("code", code)
//            .add("client_id", clientId)
//            .add("client_secret", clientSecret)
//            .add("redirect_uri", redirectUri)
//            .add("grant_type", "authorization_code")
//            .build()
//
//        // Create the request
//        val request = Request.Builder()
//            .url(tokenEndpoint)
//            .post(formBody)
//            .build()
//
//        // Make the request
//        val response: Response = client.newCall(request).execute()
//
//        if (!response.isSuccessful) {
//            throw IllegalArgumentException("Failed to exchange auth code for token: ${response.message}")
//        }
//
//        val responseBody1 = response.body?.string() ?: throw IllegalArgumentException("Empty response body")
//        println(responseBody1)
//        // Parse the response
//        val responseBody = response.body?.string() ?: throw IllegalArgumentException("Empty response body")
//        val objectMapper = ObjectMapper()
//        val tokenResponse = objectMapper.readTree(responseBody)
//
//        // Extract the access token (and optionally the refresh token)
//        val accessToken = tokenResponse["access_token"].asText()
//
//        return accessToken  // Return the access token
//    }

    private fun convertUserToResponseData(user: User): RegisterResponseData {
        return RegisterResponseData(
            id = user.id.toString(),
            username = user.username,
            email = user.email,
            created_at = user.createdAt
        )
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    private fun checkPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }

    override fun generateAndStoreToken(user: User): UserToken {
        val sub = UUID.randomUUID().toString()
        val tokenPayLoad = mapOf("sub" to sub, "username" to user.username)
        val token = Base64.getEncoder().encodeToString(ObjectMapper().writeValueAsString(tokenPayLoad).toByteArray())

        val userToken = UserToken(
            username = user.username,
            token = token,
            expiresAt = LocalDateTime.now().plusHours(1),
            user = user,
            sub = sub
        )
        tokenRepository.save(userToken)
        return userToken
    }
}