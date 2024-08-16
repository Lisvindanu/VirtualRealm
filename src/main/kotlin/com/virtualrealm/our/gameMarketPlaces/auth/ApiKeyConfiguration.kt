package com.virtualrealm.our.gameMarketPlaces.auth

import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class ApiKeyConfiguration(val apiKeyInterceptor: ApiKeyInterceptor): WebMvcConfigurationSupport() {
    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addWebRequestInterceptor(apiKeyInterceptor)
    } }