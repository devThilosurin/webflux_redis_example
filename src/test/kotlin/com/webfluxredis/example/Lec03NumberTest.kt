package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class Lec03NumberTest: BaseTest(RedissonConfig()) {

    @Test
    fun keyValueIncreaseTest() {
        val atomicLong = client.getAtomicLong("user:1:visit")
        val mono = Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1))
            .flatMap { atomicLong.incrementAndGet() }
            .doOnNext(::println)
            .then()

        StepVerifier.create(mono).verifyComplete()
    }
}