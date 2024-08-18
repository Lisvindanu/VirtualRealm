package com.virtualrealm.our.gameMarketPlaces.controller


import com.virtualrealm.our.gameMarketPlaces.model.*
import com.virtualrealm.our.gameMarketPlaces.model.authModel.*
import com.virtualrealm.our.gameMarketPlaces.service.AuthServices
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authServices: AuthServices,) {
    @PostMapping(
        value = ["/register"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )

    fun register(@RequestBody @Valid request: RegisterRequest): ResponseEntity<WebResponse<RegisterResponseData>> {
        val registerResponseData = authServices.register(request)
        val response = WebResponse(
            code = 201,
            status = "success",
            data = registerResponseData
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping(
        value = ["/login"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<WebResponse<LoginResponseData>> {
        val loginResponseData = authServices.login(request)
        val response = WebResponse(
            code = 200,
            status = "success",
            data = loginResponseData
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping(
        value = ["/logout"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun logout(@RequestBody logoutRequest: LogoutRequest): ResponseEntity<WebResponse<Nothing?>> {
        authServices.logout(logoutRequest.token)
        val response = WebResponse(
            code = 200,
            status = "success",
            data = null
        )
        return ResponseEntity.ok(response)
    }
    fun handleOptions() {
        // Handle preflight request
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



}

