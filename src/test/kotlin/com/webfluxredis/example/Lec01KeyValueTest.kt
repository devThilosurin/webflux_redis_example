package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier
import java.time.Duration

class Lec01KeyValueTest : BaseTest(RedissonConfig()) {

    @Test
    fun keyValueAccessTest() {
        val bucket = client.getBucket<String>("user:1:name", StringCodec.INSTANCE)
        val set = bucket.set("Mike")
        val get = bucket.get().doOnNext(::println).then()

        StepVerifier.create(set.then(get)).verifyComplete()
    }

    @Test
    fun keyValueExpiryTest() {
        val bucket = client.getBucket<String>("user:1:name", StringCodec.INSTANCE)
        val set = bucket.set("Mike", Duration.ofSeconds(10))
        val get = bucket.get().doOnNext(::println).then()

        StepVerifier.create(set.then(get)).verifyComplete()
    }

    @Test
    fun keyValueExtendExpiryTest() {
        val bucket = client.getBucket<String>("user:1:name", StringCodec.INSTANCE)
        val set = bucket.set("Mike", Duration.ofSeconds(10))
        val get = bucket.get().doOnNext(::println)

        // Extend expiry
        sleep(5000)
        val expire = bucket.expire(Duration.ofSeconds(10))

        StepVerifier.create(set.then(get).then(expire))
            .expectNext(true)
            .verifyComplete()

        // access expiration time
        val ttl = bucket.remainTimeToLive()
            .doOnNext(::println)
            .then()

        StepVerifier.create(ttl).verifyComplete()
    }
}