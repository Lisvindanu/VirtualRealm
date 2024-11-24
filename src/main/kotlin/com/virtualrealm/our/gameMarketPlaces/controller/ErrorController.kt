package com.virtualrealm.our.gameMarketPlaces.controller

import com.virtualrealm.our.gameMarketPlaces.error.NotFoundException
import com.virtualrealm.our.gameMarketPlaces.error.UnAuthorizedException
import com.virtualrealm.our.gameMarketPlaces.model.WebResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
@CrossOrigin
class ErrorController {
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun validationHandler(constraintViolationException: ConstraintViolationException): WebResponse<String> {
        val messages = constraintViolationException.constraintViolations.joinToString(", ") { it.message ?: "Unknown error" }
        return WebResponse(
            code = 400,
            status = "Bad Request",
            data = messages
        )
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun  notFound(notFoundException: NotFoundException): WebResponse<String> {
        return WebResponse(
            code = 404,
            status = "Not Found",
            data = "not found"
        )
    }

    @ExceptionHandler(value = [UnAuthorizedException::class])
    fun  unauthorized(notFoundException: UnAuthorizedException): WebResponse<String> {
        return WebResponse(
            code = 401,
            status = "UNAUTHORIZED",
            data = "Please Put Your Api Keys"
        )
    }
    @ExceptionHandler(value = [Exception::class])
    fun handleGenericException(exception: Exception): WebResponse<String> {
        return WebResponse(
            code = 500,
            status = "Internal Server Error",
            data = exception.message ?: "An unexpected error occurred."
        )
    }

}