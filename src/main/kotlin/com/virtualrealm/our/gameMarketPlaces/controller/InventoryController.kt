package com.virtualrealm.our.gameMarketPlaces.controller

import com.virtualrealm.our.gameMarketPlaces.model.WebResponse
import com.virtualrealm.our.gameMarketPlaces.model.inventory.GetUserInventoryItemResponse
import com.virtualrealm.our.gameMarketPlaces.model.inventory.UseInventoryItemRequest
import com.virtualrealm.our.gameMarketPlaces.service.InventoryServices
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inventory")
class InventoryController(private val inventoryServices: InventoryServices) {
    @GetMapping
    fun getUserInventory(
        @RequestParam("userId") userId: Long
    ): ResponseEntity<WebResponse<GetUserInventoryItemResponse>>{
        val response = inventoryServices.getUserInventory(userId)
        return ResponseEntity.ok(
            WebResponse(
                code = 200,
                status = response.status,
                data = response
            )
        )
    }

    @PostMapping("/use")
    fun useInventoryItem(
        @RequestBody request: UseInventoryItemRequest
    ): ResponseEntity<WebResponse<GetUserInventoryItemResponse>> {
        val response = inventoryServices.useInventoryItem(request)
        return ResponseEntity.ok(
            WebResponse(
                code = response.code,
                status = response.status,
                data = response
            )
        )
    }
}