package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import com.webfluxredis.example.dto.Student
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.LocalCachedMapOptions
import org.redisson.api.RLocalCachedMap
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.core.publisher.Flux
import java.time.Duration

class Lec08LocalCachedMapTest : BaseTest(RedissonConfig()) {
    private lateinit var studentMap: RLocalCachedMap<Int, Student>

    @BeforeAll
    fun setupClient() {
        val config = RedissonConfig()
        val redissonClient = config.getClient()

        val mapOptions = LocalCachedMapOptions.defaults<Int, Student>()
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
            .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE)

        studentMap = redissonClient.getLocalCachedMap(
            "students",
            TypedJsonJacksonCodec(Int::class.java, Student::class.java),
            mapOptions
        )
    }

    @Test
    fun appServer1() {
        val student1 = Student("sam", 10, "atlanta", listOf(1, 2, 3))
        val student2 = Student("jake", 30, "miami", listOf(10, 20, 30))
        studentMap[1] = student1
        studentMap[2] = student2

        Flux.interval(Duration.ofSeconds(1))
            .doOnNext { i -> println("$i ==> ${studentMap[1]}") }
            .subscribe()

        sleep(600000)
    }

    @Test
    fun appServer2() {
        val student1 = Student("sam-updated", 10, "atlanta", listOf(1, 2, 3))
        studentMap[1] = student1
    }
}