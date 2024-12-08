package com.virtualrealm.our.gameMarketPlaces.controller


import com.virtualrealm.our.gameMarketPlaces.entity.User
import com.virtualrealm.our.gameMarketPlaces.model.WebResponse
import com.virtualrealm.our.gameMarketPlaces.model.authModel.*
import com.virtualrealm.our.gameMarketPlaces.repository.TokenRepository
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
import com.virtualrealm.our.gameMarketPlaces.service.AuthServices
import com.virtualrealm.our.gameMarketPlaces.service.UserService
import com.virtualrealm.our.gameMarketPlaces.service.impl.AuthServicesImpl
import com.virtualrealm.our.gameMarketPlaces.service.impl.EmailService
import com.virtualrealm.our.gameMarketPlaces.service.impl.OtpService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.View
import java.util.Base64
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.SignatureException


@Controller
@CrossOrigin
@RequestMapping("/api/auth")
class AuthController(
    private val authServices: AuthServices,
    private val userService: UserService,
    private val response: HttpServletResponse,
    private val userRepository: UserRepository,
    private val error: View,
    private val otpService: OtpService,
    private val emailService: EmailService,
    tokenRepository: TokenRepository
) {
    private val logger = LoggerFactory.getLogger(AuthServicesImpl::class.java)
    @PostMapping("/register")
    fun register(@RequestBody @Valid request: RegisterRequest): ResponseEntity<WebResponse<RegisterResponseData>> {

        if(request.password != request.password_confirmation) {
            logger.error("Password and confirm password do not match!")
            throw IllegalStateException("Password and confirm password must match!")
        }

        val registerResponseData = if (request.isGoogle && !request.googleToken.isNullOrEmpty()) {
            val userData = authServices.getUserDataFromGoogleToken(request.googleToken)
            authServices.registerOrLoginWithGoogle(userData)
        } else {
            authServices.register(request)
        }

        val response = WebResponse(
            code = 201,
            status = "success",
            data = registerResponseData
        )
        return ResponseEntity.ok(response)
    }


    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") authorization: String): ResponseEntity<WebResponse<String>> {
        val token = authorization.removePrefix("Bearer ").trim() // Strip "Bearer " prefix
        authServices.logout(token)
        val response = WebResponse(
            code = 200,
            status = "success",
            data = "Successfully logged out"
        )
        return ResponseEntity.ok(response)
    }



    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<WebResponse<Any>> {
        return try {
            val user = userRepository.findByEmail(request.email)
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    WebResponse(401, "error", null, "Invalid credentials")
                )

            if (request.password.isNullOrBlank() || !checkPassword(request.password, user.password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    WebResponse(401, "error", null, "Invalid credentials")
                )
            }

            // Cek jika OTP sudah diverifikasi
            if (request.isOtpVerified) {
                val token = authServices.generateAndStoreToken(user)
                val responseData = LoginResponseData(token.token, token.expiresAt, "Login successful", "success")
                return ResponseEntity.ok(WebResponse(
                    code = 200,
                    status = "success",
                    data = responseData,
                    message = "Login successful"
                ))
            }

            // OTP handling
            val activeOtp = otpService.getActiveOtpForUser(user.id!!)
            if (request.otp == null) {
                if (activeOtp == null) {
                    val otp = otpService.generateOtp(user.id!!)
                    emailService.sendOtp(user.email, otp)
                    return ResponseEntity.ok(WebResponse(
                        code = 200,
                        status = OtpStatus.OTP_REQUIRED.status,
                        data = null,
                        message = OtpStatus.OTP_SENT.message
                    ))
                }
                return ResponseEntity.ok(WebResponse(
                    code = 200,
                    status = OtpStatus.OTP_ALREADY_SENT.status,
                    data = null,
                    message = OtpStatus.OTP_ALREADY_SENT.message
                ))
            }

            // Verify OTP
            if (!otpService.validateOtp(user.id!!, request.otp)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    WebResponse(401, "error", null, "Invalid or expired OTP")
                )
            }

            // Mark OTP as verified
            userService.markOtpAsVerified(user.id!!, request.otp!!) // Mark OTP as verified and update `isOtpVerified` to true

            // Generate token
            val token = authServices.generateAndStoreToken(user)
            val responseData = LoginResponseData(token.token, token.expiresAt, "OTP verification successful", "success")
            return ResponseEntity.ok(WebResponse(
                code = 200,
                status = OtpStatus.OTP_VERIFIED.status,
                data = responseData,
                message = OtpStatus.OTP_VERIFIED.message
            ))
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                WebResponse(500, "error", null, "An unexpected error occurred: ${e.message}")
            )
        }
    }






    @PostMapping("/send-otp")
    fun sendOtp(@RequestParam email: String): ResponseEntity<WebResponse<String>> {
        val user = userRepository.findByEmail(email) ?: throw IllegalStateException("User not found!")

        val existingOtp = otpService.getActiveOtpForUser(user.id!!)
        if (existingOtp != null) {
            otpService.deleteOtp(existingOtp)
        }

        // Generate OTP dan kirim ke email
        val otp = otpService.generateOtp(user.id!!)
        emailService.sendOtp(email, otp)

        return ResponseEntity.ok(WebResponse(
            code = 200,
            status = "success",
            data = "OTP sent to $email"
        ))
    }


    @PostMapping("/verify-otp-login")
    fun verifyOtpLogin(@RequestBody @Valid request: LoginRequest): ResponseEntity<WebResponse<LoginResponseData>> {
        return try {
            val user = userRepository.findByEmail(request.email)
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    WebResponse(401, "error", null, "User not found")
                )

            // Validate OTP
            val isValidOtp = request.otp?.let { otpService.validateOtp(user.id!!, it) } ?: false
            if (!isValidOtp) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    WebResponse(401, "error", null, "Invalid or expired OTP")
                )
            }

            userService.markOtpAsVerified(user.id!!, request.otp!!)  // Tandai OTP sebagai terverifikasi

            // Generate token
            val token = authServices.generateAndStoreToken(user)
            val responseData = LoginResponseData(token.token, token.expiresAt, "OTP verified successfully", "success")
            return ResponseEntity.ok(WebResponse(
                code = 200,
                status = "success",
                data = responseData,
                message = "OTP verification successful"
            ))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                WebResponse(500, "error", null, "An unexpected error occurred: ${e.message}")
            )
        }
    }

    @GetMapping("/user")
    fun getUserData(@RequestHeader("Authorization") authorization: String): ResponseEntity<WebResponse<UserDataResponse>> {
        val token = authorization.removePrefix("Bearer ").trim()
        try {
            // Menggunakan URL-safe Base64 decoder
            val decodedBytes = Base64.getUrlDecoder().decode(token)
            val decodedString = String(decodedBytes)
            logger.debug("Decoded token: $decodedString")

            val userData = authServices.getUserData(token)
            return ResponseEntity.ok(WebResponse(
                code = 200,
                status = "success",
                data = userData
            ))
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid token format: ${e.message}")
            return ResponseEntity.status(400).body(WebResponse(
                code = 400,
                status = "error",
                data = null // data is nullable, so it can be set to null in error cases
            ))
        }
    }


    //coba 4
    @GetMapping("/login/oauth2/code/google")
    fun googleOAuth2Callback(@RequestParam("code") code: String): ResponseEntity<WebResponse<String>> {
        logger.info("Received Google OAuth2 callback with code: $code")
        try {
            // Exchange the authorization code for an access token
            val accessToken = authServices.exchangeAuthCodeForToken(code)
            logger.info("Exchanged auth code for token: $accessToken")

            // Log the access token
            logger.debug("Access token: $accessToken")

            // Retrieve the OAuth2User using the access token
            val oauth2User = authServices.getOAuth2UserFromGoogleToken(accessToken)
            logger.info("Retrieved OAuth2 user from Google token: $oauth2User")

            // Handle Google authentication and save user
            val user = authServices.handleGoogleAuthentication(oauth2User)
            logger.info("User handled and saved: $user")

            // Redirect the user to the dashboard after successful authentication
            response.sendRedirect("http://localhost:5501/dashboard.html")
            return ResponseEntity.ok().build()

        } catch (e: Exception) {
            logger.error("OAuth2 authentication failed", e)
            val errorResponse = WebResponse<String>(
                code = 400,
                status = "error",
                data = "OAuth2 authentication failed: ${e.message}"
            )
            return ResponseEntity.status(400).body(errorResponse)
        }
    }

    fun checkPassword(inputPassword: String, storedPassword: String): Boolean {
        // Contoh menggunakan bcrypt untuk membandingkan password
        return BCrypt.checkpw(inputPassword, storedPassword)
    }

}
