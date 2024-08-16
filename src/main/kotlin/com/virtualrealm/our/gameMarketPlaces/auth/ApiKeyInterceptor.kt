package com.virtualrealm.our.gameMarketPlaces.auth

import com.virtualrealm.our.gameMarketPlaces.error.UnAuthorizedException
import com.virtualrealm.our.gameMarketPlaces.repository.ApiKeyRepository
import org.springframework.stereotype.Component
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor
import java.lang.Exception

@Component
class ApiKeyInterceptor(val apiKeyRepository: ApiKeyRepository) : WebRequestInterceptor {
    override fun preHandle(request: WebRequest) {
        val apiKey = request.getHeader("X-Api-Key")
        if (apiKey == null) {
            throw UnAuthorizedException()
        }
        if(!apiKeyRepository.existsById(apiKey)) {
            throw UnAuthorizedException()
        }
        // valid
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {
        //nothing
    }

    override fun afterCompletion(request: WebRequest, ex: Exception?) {
        //nothing
    }
}