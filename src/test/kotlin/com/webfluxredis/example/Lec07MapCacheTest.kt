package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import com.webfluxredis.example.dto.Student
import com.webfluxredis.example.util.LoggerDelegate
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class Lec07MapCacheTest : BaseTest(RedissonConfig()) {
    private val logger by LoggerDelegate()

    @Test
    fun mapCacheTest() {
        val mapCache = client.getMapCache<Int, Student>("users:cache")

        val student1 = Student("first", 27, "surin", listOf(80, 90))
        val student2 = Student("second", 25, "pathum", listOf(8, 9))

        val st1 = mapCache.put(1, student1, 5, TimeUnit.SECONDS)
        val st2 = mapCache.put(2, student2, 10, TimeUnit.SECONDS)

        StepVerifier.create(st1.then(st2).then()).verifyComplete()

        sleep(3000)

        mapCache.get(1).doOnNext { logger.info(it.toJson()) }.subscribe()
        mapCache.get(2).doOnNext { logger.info(it.toJson()) }.subscribe()

        sleep(3000)

        mapCache.get(1).doOnNext { logger.info(it.toJson()) }.subscribe()
        mapCache.get(2).doOnNext { logger.info(it.toJson()) }.subscribe()
    }
}