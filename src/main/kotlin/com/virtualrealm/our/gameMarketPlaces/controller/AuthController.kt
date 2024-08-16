package com.virtualrealm.our.gameMarketPlaces.controller


import com.virtualrealm.our.gameMarketPlaces.model.*
import com.virtualrealm.our.gameMarketPlaces.repository.TokenRepository
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
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
    fun logout(@RequestHeader("X-Api-Key") token: String): ResponseEntity<WebResponse<Nothing?>> {
        authServices.logout(token)
        val response = WebResponse(
            code = 200,
            status = "success",
            data = null
        )
        return ResponseEntity.ok(response)
    }

}

