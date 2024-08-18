package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.virtualrealm.our.gameMarketPlaces.entity.Purchase
import com.virtualrealm.our.gameMarketPlaces.model.purchase.PurchaseRequest
import com.virtualrealm.our.gameMarketPlaces.repository.ProductRepository
import com.virtualrealm.our.gameMarketPlaces.repository.PurchaseRepository
import com.virtualrealm.our.gameMarketPlaces.repository.UserRepository
import com.virtualrealm.our.gameMarketPlaces.service.PurchaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PurchaseServiceImpl @Autowired constructor(
    private val purchaseRepository: PurchaseRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    repository: PurchaseRepository
)
    : PurchaseService {
    override fun createPurchase(request: PurchaseRequest): Purchase {
        val product = productRepository.findById(request.productId).orElseThrow { RuntimeException("Product not found") }
        val user = userRepository.findById(request.userId).orElseThrow { RuntimeException("User not found") } // Menambahkan user
        val totalPrice = product.price * request.quantity
        val purchase = Purchase(
            user = user,
            product = product,
            quantity = request.quantity,
            totalPrice = totalPrice,
        )
        return purchaseRepository.save(purchase)
    }

    override fun getPurchaseHistory(userId: Long): List<Purchase> {
        return purchaseRepository.findByUserId(userId)
    }
}