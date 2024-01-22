package com.webfluxredis.example.controller

import com.webfluxredis.example.entity.Product
import com.webfluxredis.example.service.ProductServiceV1
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("product/v1")
class ProductControllerV1(
    private val productService: ProductServiceV1
) {

    @GetMapping("{id}")
    fun getProduct(@PathVariable id: Int): Mono<Product> =
        productService.getProduct(id)

    @PutMapping("{id}")
    fun updateProduct(
        @PathVariable id: Int, @RequestBody productMono: Mono<Product>
    ): Mono<Product> = productService.updateProduct(id, productMono)
}