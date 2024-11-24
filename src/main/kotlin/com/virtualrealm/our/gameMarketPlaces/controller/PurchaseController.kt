package com.virtualrealm.our.gameMarketPlaces.controller

import com.virtualrealm.our.gameMarketPlaces.entity.Purchase
import com.virtualrealm.our.gameMarketPlaces.model.WebResponse
import com.virtualrealm.our.gameMarketPlaces.model.purchase.PurchaseRequest
import com.virtualrealm.our.gameMarketPlaces.model.purchase.PurchaseResponse
import com.virtualrealm.our.gameMarketPlaces.repository.PurchaseRepository
import com.virtualrealm.our.gameMarketPlaces.service.PurchaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/purchases")
class PurchaseController (val purchaseService: PurchaseService) {
    @PostMapping(
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun create(@RequestBody request: PurchaseRequest) : WebResponse<Purchase> {
        val purchase = purchaseService.createPurchase(request)
        val purchaseResponse = PurchaseResponse(
            purchaseId = purchase.id!!,
            userId = purchase.user.id!!,
            productId = purchase.product.id!!,
            quantity = purchase.quantity,
            totalPrice = purchase.totalPrice,
            createdAt = purchase.createdAt
        )
        return WebResponse(
            code = 200,
            status = "success",
            data = purchase
        )
    }

    @GetMapping(
        produces = ["application/json"]
    )
    fun getPurchaseHistory(@RequestParam("userId") userId: Long): WebResponse<List<PurchaseResponse>> {
        val purchases = purchaseService.getPurchaseHistory(userId)
        val purchaseResponses = purchases.map { purchase ->
            PurchaseResponse(
                purchaseId = purchase.id!!,
                userId = purchase.user.id!!,
                productId = purchase.product.id!!,
                quantity = purchase.quantity,
                totalPrice = purchase.totalPrice,
                createdAt = purchase.createdAt
            )
        }
        return WebResponse(
            code = 200,
            status = "success",
            data = purchaseResponses
        )
    }
}


