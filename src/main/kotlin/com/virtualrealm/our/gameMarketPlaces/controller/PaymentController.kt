package com.virtualrealm.our.gameMarketPlaces.controller

import com.virtualrealm.our.gameMarketPlaces.entity.ApiKey
import com.virtualrealm.our.gameMarketPlaces.error.PaymentAlreadyConfirmedException
import com.virtualrealm.our.gameMarketPlaces.model.WebResponse
import com.virtualrealm.our.gameMarketPlaces.model.payment.*
import com.virtualrealm.our.gameMarketPlaces.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(val paymentService: PaymentService) {
    @PostMapping(
        path = ["/initiate"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun initiatePayment(@RequestBody request: InitiatePaymentRequest, @RequestHeader("X-Api-Key") apiKey: String): WebResponse<InitiatePaymentResponse> {
        val payment = paymentService.initiatePayment(request)
        val paymentResponse = InitiatePaymentResponse(
            paymentId = payment.id!!,
            status = payment.status,
            amount = payment.amount,
            createdAt = payment.createdAt
        )
        return WebResponse(
            code = 200,
            status = "success",
            data = paymentResponse
        )
    }

    @PutMapping(
        path = ["/confirm/{paymentId}"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun confirmPayment(
        @PathVariable paymentId: Long,
        @RequestBody request: ConfirmPaymentRequest,
        @RequestHeader("X-Api-Key") apiKey: String
    ) : WebResponse<ConfirmPaymentResponse> {
        val payment = paymentService.confirmPayment(paymentId, request)
        val paymentResponse = ConfirmPaymentResponse(
            paymentId = payment.id!!,
            status = payment.status,
            confirmedAt = payment.confirmedAt!!
        )
        return WebResponse(
            code = 200,
            status = "success",
            data = paymentResponse
        )
    }

    @PutMapping(
        path = ["/cancel/{paymentId}"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun cancelPayment(
        @PathVariable paymentId: Long,
        @RequestBody request: CancelPaymentRequest,
        @RequestHeader("X-Api-Key") apiKey: String
    ) : WebResponse<Void?> {
        paymentService.cancelPayment(paymentId, request)
        return WebResponse(
            code = 200,
            status = "success",
            data = null
        )
    }
    @GetMapping(
        path = ["/{paymentId}"],
        produces = ["application/json"]
    )
    fun getPaymentDetails(
        @PathVariable paymentId: Long,
        @RequestHeader("X-Api-Key") apiKey: String
    ): WebResponse<PaymentDetailResponse> {
        val payment = paymentService.getPaymentDetails(paymentId)
        val paymentResponse = PaymentDetailResponse(
            paymentId = payment.id!!,
            amount = payment.amount,
            status = payment.status,
            createdAt = payment.createdAt,
            confirmedAt = payment.confirmedAt,
            canceledAt = payment.canceledAt,
            reason = payment.reason
        )
        return WebResponse(
            code = 200,
            status = "success",
            data = paymentResponse
        )
    }

    @ExceptionHandler(PaymentAlreadyConfirmedException::class)
    fun handlePaymentAlreadyConfirmedException(ex: PaymentAlreadyConfirmedException): ResponseEntity<WebResponse<Void?>> {
        val response = WebResponse<Void?>(
            code = HttpStatus.BAD_REQUEST.value(),
            status = ex.message ?: "Gabisa Cancel Bos",
            data = null

        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<WebResponse<Void?>> {
        val response = WebResponse<Void?>(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            status = ex.message ?: "Internal Server Error",
            data = null
        )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}