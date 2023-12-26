package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import com.webfluxredis.example.util.LoggerDelegate
import org.junit.jupiter.api.Test
import org.redisson.api.DeletedObjectListener
import org.redisson.api.ExpiredObjectListener
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier
import java.time.Duration

class Lec05EventListenerTest : BaseTest(RedissonConfig()) {
    private val logger by LoggerDelegate()

    @Test
    fun expiredEventTest() {
        val bucket = client.getBucket<String>("user:1:name", StringCodec.INSTANCE)
        val set = bucket.set("Mike", Duration.ofSeconds(10))
        val get = bucket.get().doOnNext(logger::info).then()
        val event = bucket.addListener(object : ExpiredObjectListener {
            override fun onExpired(s: String?) {
                logger.info("Key expired: $s")
            }
        }).then()

        StepVerifier.create(set.then(get).then(event)).verifyComplete()
        sleep(11000)
    }

    @Test
    fun deletedEventTest() {
        val bucket = client.getBucket<String>("user:1:name", StringCodec.INSTANCE)
        val set = bucket.set("Mike")
        val get = bucket.get().doOnNext(logger::info).then()
        val event = bucket.addListener(object : DeletedObjectListener {
            override fun onDeleted(name: String?) {
                logger.info("Key deleted: $name")
            }
        }).then()

        StepVerifier.create(set.then(get).then(event)).verifyComplete()
        sleep(30000)
    }
}