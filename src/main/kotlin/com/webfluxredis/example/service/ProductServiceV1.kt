package com.webfluxredis.example.service

import com.webfluxredis.example.entity.Product
import com.webfluxredis.example.repository.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductServiceV1(
    private val productRepo: ProductRepository
) {

    fun getProduct(id: Int): Mono<Product> = productRepo.findById(id)

    fun updateProduct(id: Int, productMono: Mono<Product>): Mono<Product> =
        productRepo.findById(id)
            .flatMap { productMono.doOnNext { p -> p.id = id } }
            .flatMap(productRepo::save)
}