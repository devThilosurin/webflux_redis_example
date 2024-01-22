package com.webfluxredis.example.controller

import com.webfluxredis.example.entity.Product
import com.webfluxredis.example.service.ProductServiceV2
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("product/v2")
class ProductControllerV2(
    private val productService: ProductServiceV2
) {

    @GetMapping("{id}")
    fun getProduct(@PathVariable id: Int): Mono<Product> =
        productService.getProduct(id)

    @PutMapping("{id}")
    fun updateProduct(
        @PathVariable id: Int, @RequestBody productMono: Mono<Product>
    ): Mono<Product> = productService.updateProduct(id, productMono)

    @DeleteMapping("{id}")
    fun deleteProduct(@PathVariable id: Int): Mono<Void> =
        productService.deleteProduct(id)
}