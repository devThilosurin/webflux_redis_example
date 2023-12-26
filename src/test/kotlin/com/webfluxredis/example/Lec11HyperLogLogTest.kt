package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.Test
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.stream.LongStream

class Lec11HyperLogLogTest : BaseTest(RedissonConfig()) {

    @Test
    fun count() {
        val counter = client.getHyperLogLog<Long>("user:visit", LongCodec.INSTANCE)
        val long1 = LongStream.rangeClosed(1, 25_000).boxed().toList()
        val long2 = LongStream.rangeClosed(25_001, 50_000).boxed().toList()
        val long3 = LongStream.rangeClosed(1, 75_000).boxed().toList()
        val long4 = LongStream.rangeClosed(50_000, 100_000).boxed().toList()

        val mono = Flux.just(long1, long2, long3, long4)
            .flatMap(counter::addAll)
            .then()
        StepVerifier.create(mono).verifyComplete()
        counter.count().doOnNext(::println).subscribe()
    }
}