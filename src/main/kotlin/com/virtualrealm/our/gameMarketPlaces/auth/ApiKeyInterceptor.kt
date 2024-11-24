package com.virtualrealm.our.gameMarketPlaces.auth

import com.virtualrealm.our.gameMarketPlaces.error.UnAuthorizedException
import com.virtualrealm.our.gameMarketPlaces.repository.ApiKeyRepository
import org.springframework.stereotype.Component
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor
import org.springframework.web.servlet.HandlerInterceptor

@Component
class ApiKeyInterceptor(val apiKeyRepository: ApiKeyRepository) : WebRequestInterceptor, HandlerInterceptor {
    override fun preHandle(request: WebRequest) {
        val apiKey = request.getHeader("X-Api-Key")
        println("Interceptor aktif. Api key: $apiKey") // Debug

        if (apiKey == null) {
            println("Api key tidak ditemukan") // Debug
            throw UnAuthorizedException("API key tidak ditemukan")
        }

        if (!apiKeyRepository.existsById(apiKey)) {
            println("Api key tidak valid: $apiKey") // Debug
            throw UnAuthorizedException("API key tidak valid")
        }

        println("API key valid: $apiKey") // Debug
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {}
    override fun afterCompletion(request: WebRequest, ex: Exception?) {}

}

