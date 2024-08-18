package com.virtualrealm.our.gameMarketPlaces.repository

import com.virtualrealm.our.gameMarketPlaces.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, String> {
}