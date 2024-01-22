package com.webfluxredis.example.service.util

import reactor.core.publisher.Mono

abstract class CacheTemplate<KEY, ENTITY : Any> {
    fun get(key: KEY): Mono<ENTITY> =
        getFromCache(key)
            .switchIfEmpty(
                getFromSource(key)
                    .flatMap { e -> updateCache(key, e) }
            )

    fun update(key: KEY, entity: ENTITY): Mono<ENTITY> =
        updateSource(key, entity)
            .flatMap { e -> deleteFromCache(key).thenReturn(e) }

    fun delete(key: KEY): Mono<Void> =
        deleteFromSource(key)
            .then(deleteFromCache(key))

    protected abstract fun getFromSource(key: KEY): Mono<ENTITY>
    protected abstract fun getFromCache(key: KEY): Mono<ENTITY>
    protected abstract fun updateSource(key: KEY, entity: ENTITY): Mono<ENTITY>
    protected abstract fun updateCache(key: KEY, entity: ENTITY): Mono<ENTITY>
    protected abstract fun deleteFromSource(key: KEY): Mono<Void>
    protected abstract fun deleteFromCache(key: KEY): Mono<Void>
}