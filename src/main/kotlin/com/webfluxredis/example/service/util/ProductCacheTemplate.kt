package com.webfluxredis.example.service.util

import com.webfluxredis.example.entity.Product
import com.webfluxredis.example.repository.ProductRepository
import org.redisson.api.RMapReactive
import org.redisson.api.RedissonReactiveClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductCacheTemplate(
    private val productRepo: ProductRepository,
    redissonClient: RedissonReactiveClient
) : CacheTemplate<Int, Product>() {
    private val map: RMapReactive<Int, Product> = redissonClient.getMap("product")

    override fun getFromSource(key: Int): Mono<Product> = productRepo.findById(key)

    override fun getFromCache(key: Int): Mono<Product> = map.get(key)

    override fun updateSource(key: Int, entity: Product): Mono<Product> =
        productRepo.findById(key)
            .flatMap {
                entity.id = key
                productRepo.save(entity)
            }

    override fun updateCache(key: Int, entity: Product): Mono<Product> =
        map.fastPut(key, entity).thenReturn(entity)

    override fun deleteFromSource(key: Int): Mono<Void> =
        productRepo.deleteById(key)

    override fun deleteFromCache(key: Int): Mono<Void> =
        map.fastRemove(key).then()
}