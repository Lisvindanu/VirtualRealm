package com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateProductRequest(

    val id: Long? = null,

    @field:NotBlank
    val name: String?,

    @field:NotNull
    @field:Min(value = 1)
    val price: Long?,

    @field:NotNull
    @field:Min(value = 0)
    val quantity: Int?,

    @field:NotNull(message = "Category ID must not be null")
    val categoryId: Long?,

    val genreId: Long? // Opsional, hanya diperlukan untuk kategori tertentu (misalnya, Game)
)
