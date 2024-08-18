package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository: JpaRepository<Payment, Long> {
    fun findByPurchaseId(purchaseId: Long): List<Payment>
    fun findByStatus(status: String): List<Payment>
    fun findByStatusAndPurchaseId(status: String,purchaseId: Long): Payment?
}