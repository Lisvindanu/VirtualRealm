package com.virtualrealm.our.gameMarketPlaces.auth

import com.virtualrealm.our.gameMarketPlaces.error.UnAuthorizedException
import com.virtualrealm.our.gameMarketPlaces.repository.ApiKeyRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyFilter(private val apiKeyRepository: ApiKeyRepository) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val apiKey = request.getHeader("X-Api-Key")
        if (apiKey == null || !apiKeyRepository.existsById(apiKey)) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.writer.write("Forbidden: Invalid API Key")
            return
        }
        filterChain.doFilter(request, response)
    }
}
