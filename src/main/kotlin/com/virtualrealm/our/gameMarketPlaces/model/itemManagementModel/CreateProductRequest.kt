package com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateProductRequest(
    @field:NotNull
    val id:Long?,
    @field:NotBlank
    val name:String?,
    @field:NotNull
    @field:Min(value = 1)
    val price: Long?,
    @field:Min(value = 0)
    val quantity:Int?
)
