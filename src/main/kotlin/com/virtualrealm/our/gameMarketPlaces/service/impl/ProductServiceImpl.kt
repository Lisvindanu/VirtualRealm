package com.virtualrealm.our.gameMarketPlaces.service.impl

import com.virtualrealm.our.gameMarketPlaces.entity.Product
import com.virtualrealm.our.gameMarketPlaces.error.NotFoundException
import com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel.CreateProductRequest
import com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel.ListProductRequest
import com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel.ProductResponse
import com.virtualrealm.our.gameMarketPlaces.model.itemManagementModel.UpdateProductRequest
import com.virtualrealm.our.gameMarketPlaces.repository.ProductRepository
import com.virtualrealm.our.gameMarketPlaces.service.ProductService
import com.virtualrealm.our.gameMarketPlaces.validation.ValidationUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class ProductServiceImpl (
    val productRepository: ProductRepository,
    val validationUtil: ValidationUtil) : ProductService {
    override fun create(createProductRequest: CreateProductRequest): ProductResponse {
        validationUtil.validate(createProductRequest)
        val product = Product(
            id = createProductRequest.id !!,
            name = createProductRequest.name !!,
            price = createProductRequest.price!!,
            quantity = createProductRequest.quantity!!,
            createdAt = Date(),
            updatedAt = Date()
        )
        productRepository.save(product)
        return convertProductToProductResponse(product)
    }

    override fun get(id: Long): ProductResponse {
        val product = findProductByIdOrThrowNotFound(id)
        return convertProductToProductResponse(product)
    }

    override fun update(id: Long, updateProductRequest: UpdateProductRequest): ProductResponse {
        val product = findProductByIdOrThrowNotFound(id)
        validationUtil.validate(updateProductRequest)
        product.apply {
            name = updateProductRequest.name!!
            price = updateProductRequest.price!!
            quantity = updateProductRequest.quantity!!
            updatedAt = Date()
        }
        productRepository.save(product)
        return convertProductToProductResponse(product)
    }

    override fun delete(id: Long) {
       val product = findProductByIdOrThrowNotFound(id)
        productRepository.delete(product)
    }

    override fun list(listProductRequest: ListProductRequest): List<ProductResponse> {
       val page = productRepository.findAll(PageRequest.of(listProductRequest.page, listProductRequest.size))
        val product : List<Product> = page.get().collect(Collectors.toList())
        return product.map { convertProductToProductResponse(it) }
    }

    private fun findProductByIdOrThrowNotFound(id: Long): Product {
        val product = productRepository.findByIdOrNull(id)
        if(product == null) {
            throw NotFoundException()
        } else {
            return product
        }
    }


    private fun convertProductToProductResponse(product: Product): ProductResponse {
        return ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            quantity = product.quantity,
            created_at = product.createdAt,
            updated_at = product.updatedAt
        )
    }

}
