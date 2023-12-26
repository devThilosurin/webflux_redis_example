package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import com.webfluxredis.example.util.LoggerDelegate
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier

class Lec04BucketAsMapTest : BaseTest(RedissonConfig()) {
    private val logger by LoggerDelegate()

    @Test
    fun bucketsAsMap() {
        val mono = client.getBuckets(StringCodec.INSTANCE)
            .get<String>("user:1:name", "user:2:name", "user:3:name")
            .map(Map<String, String>::toString)
            .doOnNext(logger::info)
            .then()
        StepVerifier.create(mono).verifyComplete()
    }
}