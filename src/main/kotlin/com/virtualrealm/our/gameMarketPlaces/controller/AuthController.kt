package com.virtualrealm.our.gameMarketPlaces.controller


import com.virtualrealm.our.gameMarketPlaces.model.WebResponse
import com.virtualrealm.our.gameMarketPlaces.model.authModel.*
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
import com.virtualrealm.our.gameMarketPlaces.service.AuthServices
import com.virtualrealm.our.gameMarketPlaces.service.UserService
import com.virtualrealm.our.gameMarketPlaces.service.impl.AuthServicesImpl
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.View
import java.util.Base64


@Controller
@CrossOrigin
@RequestMapping("/api/auth")
class AuthController(
    private val authServices: AuthServices,
    private val userService: UserService,
    private val response: HttpServletResponse,
    private val userRepository: UserRepository,
    private val error: View
) {
    private val logger = LoggerFactory.getLogger(AuthServicesImpl::class.java)
    @PostMapping("/register")
    fun register(@RequestBody @Valid request: RegisterRequest): ResponseEntity<WebResponse<RegisterResponseData>> {
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

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<WebResponse<LoginResponseData>> {
        val loginResponseData = if (request.isGoogle && !request.googleToken.isNullOrEmpty()) {
            val userData = authServices.getUserDataFromGoogleToken(request.googleToken)
            authServices.login(LoginRequest(username = userData.username, password = ""))
        } else {
            authServices.login(request)
        }

        val response = WebResponse(
            code = 200,
            status = "success",
            data = loginResponseData
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<WebResponse<String>> {
        authServices.logout(token)
        val response = WebResponse(
            code = 200,
            status = "success",
            data = "Successfully logged out"
        )
        return ResponseEntity.ok(response)
    }

//    @GetMapping("/user")
//    fun getUserData(@RequestHeader("Authorization") authorization: String): ResponseEntity<WebResponse<UserDataResponse>> {
//        val token = authorization.removePrefix("Bearer ").trim()
//        val userData = authServices.getUserData(token)
//        return ResponseEntity.ok(WebResponse(
//            code = 200,
//            status = "success",
//            data = userData
//        ))
//    }
//@GetMapping("/user")
//fun getUserData(@RequestHeader("Authorization") authorization: String): ResponseEntity<WebResponse<UserDataResponse>> {
//    // Ambil token dari Authorization header
//    val token = authorization.removePrefix("Bearer ").trim()
//
//    // Validasi token (misalnya dengan meng-decode dan memverifikasi JWT)
//    if (token.isBlank()) {
//        throw IllegalArgumentException("Token is missing or invalid")
//    }
//
//    // Misalnya, dekode token dan dapatkan informasi user
//    val decodedToken = decodeToken(token)  // Fungsi ini bisa meng-decode token dan memverifikasi isinya
//    val googleId = decodedToken["sub"] as? String
//        ?: throw IllegalArgumentException("Google ID not found in token")
//
//    // Ambil data user dari database berdasarkan googleId
//    val user = userService.findByGoogleId(googleId) ?: throw IllegalArgumentException("User not found")
//
//    // Buat response data user
//    val userDataResponse = UserDataResponse(
//        username = user.username,
//        email = user.email,
//        googleId = googleId
//    )
//
//    // Kembalikan response dengan data user
//    return ResponseEntity.ok(WebResponse(
//        code = 200,
//        status = "success",
//        data = userDataResponse
//    ))
//}
//
//    // Fungsi untuk decode JWT token (contoh implementasi)
//    fun decodeToken(token: String): Map<String, Any> {
//        // Gunakan pustaka seperti jjwt untuk decode JWT dan memverifikasi isinya
//        val claims = Jwts.parserBuilder()
//            .setSigningKey("your-secret-key")  // Gantilah dengan kunci yang sesuai
//            .build()
//            .parseClaimsJws(token)
//            .body
//
//        // Kembalikan claims sebagai Map untuk mengambil sub (Google ID)
//        return claims
//            .toMap()
//    }




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



//coba1
//    @GetMapping("/login/oauth2/code/google")
//    fun googleOAuth2Callback(@RequestParam("code") code: String): ResponseEntity<WebResponse<String>> {
//        try {
//            // Exchange the authorization code for an access token
//            val accessToken = authServices.exchangeAuthCodeForToken(code)
//
//            // Get user data from the Google token
//            val userData = authServices.getUserDataFromGoogleToken(accessToken)
//
//            // Register or log the user in using the retrieved Google user data
//            authServices.registerOrLoginWithGoogle(userData)
//
//            // Redirect the user to the dashboard after successful authentication
//            response.sendRedirect("http://localhost:5501/dashboard.html")
//
//            // Return a successful response
//            return ResponseEntity.ok().build()
//
//        } catch (e: Exception) {
//            // Return an error response if OAuth2 authentication fails
//            val errorResponse = WebResponse<String>(
//                code = 400,
//                status = "error",
//                data = "OAuth2 authentication failed: ${e.message}"
//            )
//            return ResponseEntity.status(400).body(errorResponse)
//        }
//    }


    //coba 2
//    @GetMapping("/login/oauth2/code/google")
//    fun googleOAuth2Callback(@RequestParam("code") code: String): ResponseEntity<WebResponse<String>> {
//        logger.info("Received Google OAuth2 callback with code: $code")
//        try {
//            val accessToken = authServices.exchangeAuthCodeForToken(code)
//            logger.info("Exchanged auth code for token: $accessToken")
//
//            val userData = authServices.getUserDataFromGoogleToken(accessToken)
//            logger.info("Retrieved user data from Google token: $userData")
//
//            authServices.registerOrLoginWithGoogle(userData)
//            logger.info("User registered or logged in with Google data")
//
//            response.sendRedirect("http://localhost:5501/dashboard.html")
//            return ResponseEntity.ok().build()
//
//        } catch (e: Exception) {
//            logger.error("OAuth2 authentication failed", e)
//            val errorResponse = WebResponse<String>(
//                code = 400,
//                status = "error",
//                data = "OAuth2 authentication failed: ${e.message}"
//            )
//            return ResponseEntity.status(400).body(errorResponse)
//        }
//    }
//

//coba3
//    @GetMapping("/login/oauth2/code/google")
//    fun googleOAuth2Callback(@RequestParam("code") code: String): ResponseEntity<WebResponse<String>> {
//        try {
//
//            val accessToken = authServices.exchangeAuthCodeForToken(code)
//
//
//            val userData = authServices.getUserDataFromGoogleToken(accessToken)
//
//
//            val registerResponseData = authServices.registerOrLoginWithGoogle(userData)
//
//
//            response.sendRedirect("http://localhost:5501/dashboard.html")
//            return ResponseEntity.ok().build()
//
//            val response = WebResponse(
//                code = 200,
//                status = "success",
//                data = "User successfully logged in or registered with Google"
//            )
//            return ResponseEntity.ok(response)
//
//        } catch (e: Exception) {
//
//            val errorResponse = WebResponse<String>(
//                code = 400,
//                status = "error",
//                data = "OAuth2 authentication failed: ${e.message}"
//            )
//            return ResponseEntity.status(400).body(errorResponse)
//        }
//    }



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



}
