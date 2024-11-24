package com.virtualrealm.our.gameMarketPlaces.controller

import com.virtualrealm.our.gameMarketPlaces.entity.User
import com.virtualrealm.our.gameMarketPlaces.model.WebResponse
import com.virtualrealm.our.gameMarketPlaces.model.authModel.*
import com.virtualrealm.our.gameMarketPlaces.service.AuthServices
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
class AuthController(private val authServices: AuthServices) {

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

    @GetMapping("/user")
    fun getUserData(@RequestHeader("Authorization") authorization: String): ResponseEntity<WebResponse<UserDataResponse>> {
        val token = authorization.removePrefix("Bearer ").trim()
        val userData = authServices.getUserData(token)
        return ResponseEntity.ok(WebResponse(
            code = 200,
            status = "success",
            data = userData
        ))
    }



    @GetMapping("/login/oauth2/code/google")
    fun googleOAuth2Callback(@RequestParam("code") code: String): ResponseEntity<WebResponse<String>> {
        try {

            val accessToken = authServices.exchangeAuthCodeForToken(code)


            val userData = authServices.getUserDataFromGoogleToken(accessToken)


            val registerResponseData = authServices.registerOrLoginWithGoogle(userData)

            val response = WebResponse(
                code = 200,
                status = "success",
                data = "User successfully logged in or registered with Google"
            )
            return ResponseEntity.ok(response)

        } catch (e: Exception) {

            val errorResponse = WebResponse<String>(
                code = 400,
                status = "error",
                data = "OAuth2 authentication failed: ${e.message}"
            )
            return ResponseEntity.status(400).body(errorResponse)
        }
    }
}
