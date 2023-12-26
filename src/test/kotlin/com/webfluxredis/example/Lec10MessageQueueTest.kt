package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.RBlockingQueueReactive
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class Lec10MessageQueueTest : BaseTest(RedissonConfig()) {
    private lateinit var msgQueue: RBlockingQueueReactive<Long>

    @BeforeAll
    fun setUpQueue() {
        msgQueue = client.getBlockingQueue("message-queue", LongCodec.INSTANCE)
    }

    @Test
    fun consumer1() {
        msgQueue.takeElements()
            .doOnNext { println("Consumer 1: $it") }
            .subscribe()
        sleep(600_000)
    }

    @Test
    fun consumer2() {
        msgQueue.takeElements()
            .doOnNext { println("Consumer 1: $it") }
            .subscribe()
        sleep(600_000)
    }

    @Test
    fun producer() {
        val mono = Flux.range(1, 100)
            .delayElements(Duration.ofMillis(500))
            .doOnNext { println("Going to add: $it") }
            .flatMap { msgQueue.add(it.toLong()) }
            .then()

        StepVerifier.create(mono)
            .verifyComplete()
    }
}