package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.virtualrealm.our.gameMarketPlaces.entity.Payment
import com.virtualrealm.our.gameMarketPlaces.error.PaymentAlreadyConfirmedException
import com.virtualrealm.our.gameMarketPlaces.model.payment.CancelPaymentRequest
import com.virtualrealm.our.gameMarketPlaces.model.payment.ConfirmPaymentRequest
import com.virtualrealm.our.gameMarketPlaces.model.payment.InitiatePaymentRequest
import com.virtualrealm.our.gameMarketPlaces.repository.PaymentRepository
import com.virtualrealm.our.gameMarketPlaces.repository.PurchaseRepository
import com.virtualrealm.our.gameMarketPlaces.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PaymentServiceImpl @Autowired constructor(
    private val paymentRepository: PaymentRepository,
    private val purchaseRepository: PurchaseRepository
) : PaymentService {
    override fun initiatePayment(request: InitiatePaymentRequest): Payment {
        val purchase = purchaseRepository.findById(request.purchaseId!!).orElseThrow {
            RuntimeException("Purchase not found")
        }
        val payment = Payment(
            purchase = purchase,
            amount = request.amount,
            status = "initiated",
            createdAt = Date()
        )
        return paymentRepository.save(payment)
    }


    override fun confirmPayment(paymentId: Long, request: ConfirmPaymentRequest): Payment {
        val payment = paymentRepository.findById(paymentId).orElseThrow {
            RuntimeException("Payment not found")
        }
        if (payment.status == "cancelled") {
            throw PaymentAlreadyConfirmedException("Cannot confirm a canceled payment")
        }
        payment.status = request.status
        payment.confirmedAt = if(request.status == "confirmed") Date() else null
        return paymentRepository.save(payment)
    }

    override fun cancelPayment(paymentId: Long, request: CancelPaymentRequest): Payment {
        val payment = paymentRepository.findById(paymentId).orElseThrow {
            RuntimeException("Payment not found")
        }

        if (payment.status == "confirmed") {
            throw PaymentAlreadyConfirmedException("Cannot cancel a confirmed payment")
        }

        payment.status = "cancelled"
        payment.canceledAt = Date()
        payment.reason = request.reason
        return paymentRepository.save(payment)
    }


    override fun getPaymentDetails(paymentId: Long): Payment {
        return paymentRepository.findById(paymentId).orElseThrow {
            RuntimeException("Payment not found")
        }
    }
}