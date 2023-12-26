package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.redisson.api.RedissonReactiveClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest(private val redissonConfig: RedissonConfig) {
    protected lateinit var client: RedissonReactiveClient

    @BeforeAll
    fun setClient() {
        client = redissonConfig.getRedissonClient()
    }

    @AfterAll
    fun shutdownClient() {
        client.shutdown()
    }

    protected fun sleep(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}