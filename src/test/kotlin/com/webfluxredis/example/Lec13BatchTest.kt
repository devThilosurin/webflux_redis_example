package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.Test
import org.redisson.api.BatchOptions
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class Lec13BatchTest : BaseTest(RedissonConfig()) {

    @Test
    fun batchTest() {
        val batch = client.createBatch(BatchOptions.defaults())
        val list = batch.getList<Long>("numbers-list", LongCodec.INSTANCE)
        val set = batch.getSet<Long>("numbers-set", LongCodec.INSTANCE)

        for (i in 1..500_000) {
            list.add(i.toLong())
            set.add(i.toLong())
        }

        StepVerifier.create(batch.execute().then())
            .verifyComplete()
    }

    @Test
    fun regularTest() {
        val list = client.getList<Long>("numbers-list", LongCodec.INSTANCE)
        val set = client.getSet<Long>("numbers-set", LongCodec.INSTANCE)
        val mono = Flux.range(1, 500_000)
            .map(Int::toLong)
            .flatMap { i -> list.add(i).then(set.add(i)) }
            .then()
        StepVerifier.create(mono).verifyComplete()
    }
}