package com.webfluxredis.example.service

import com.webfluxredis.example.entity.Product
import com.webfluxredis.example.service.util.CacheTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductServiceV2(
    private val cacheTemplate: CacheTemplate<Int, Product>
) {

    fun getProduct(id: Int): Mono<Product> = cacheTemplate.get(id)

    fun updateProduct(id: Int, productMono: Mono<Product>): Mono<Product> =
        productMono.flatMap { product -> cacheTemplate.update(id, product) }

    fun deleteProduct(id: Int): Mono<Void> = cacheTemplate.delete(id)


}