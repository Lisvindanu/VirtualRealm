package com.virtualrealm.our.gameMarketPlaces.service

import com.virtualrealm.our.gameMarketPlaces.entity.Payment
import com.virtualrealm.our.gameMarketPlaces.model.payment.CancelPaymentRequest
import com.virtualrealm.our.gameMarketPlaces.model.payment.ConfirmPaymentRequest
import com.virtualrealm.our.gameMarketPlaces.model.payment.InitiatePaymentRequest

interface PaymentService {
    fun initiatePayment(request: InitiatePaymentRequest): Payment
    fun confirmPayment(paymentId: Long, request: ConfirmPaymentRequest): Payment
    fun cancelPayment(paymentId: Long, request: CancelPaymentRequest): Payment
    fun getPaymentDetails(paymentId: Long): Payment
}