package com.webfluxredis.example

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.webfluxredis.example.config.RedissonConfig
import com.webfluxredis.example.dto.Student
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.test.StepVerifier

class Lec06MapTest : BaseTest(RedissonConfig()) {

    @Test
    fun mapTest1() {
        val map = client.getMap<String, String>("user:1", StringCodec.INSTANCE)
        val name = map.put("name", "sam")
        val age = map.put("age", "26")
        val city = map.put("city", "surin")
        StepVerifier.create(name.then(age).then(city)).verifyComplete()
    }

    @Test
    fun mapTest2() {
        val map = client.getMap<String, String>("user:2", StringCodec.INSTANCE)
        val kotlinMap = mapOf(
            "name" to "jake",
            "age" to "27",
            "city" to "pathum thani"
        )
        StepVerifier.create(map.putAll(kotlinMap)).verifyComplete()
    }

    @Test
    fun mapTest3() {
        val codec = TypedJsonJacksonCodec(Int::class.java, Student::class.java, jsonMapper())
        val map = client.getMap<Int, Student>("users", codec)
        val student1 = Student("jake", 27, "surin", listOf(80, 90, 100))
        val student2 = Student("mike", 24, "pathum thani", listOf(8, 9, 10))
        val mono1 = map.put(1, student1)
        val mono2 = map.put(2, student2)
        StepVerifier.create(mono1.then(mono2)).verifyComplete()
    }
}